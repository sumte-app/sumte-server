package com.sumte.room.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.sumte.reservation.entity.Reservation;
import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.image.entity.Image;
import com.sumte.image.entity.OwnerType;
import com.sumte.image.repository.ImageRepository;
import com.sumte.reservation.repository.ReservationRepository;
import com.sumte.room.converter.RoomConverter;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomQueryServiceImpl implements RoomQueryService {

	private final RoomRepository roomRepository;
	private final RoomConverter roomConverter;
	private final ReservationRepository reservationRepository;
	private final ImageRepository imageRepository;

	@Override
	@Transactional
	public RoomResponseDTO.GetRoomResponse getRoomById(Long roomId) {
		Room room = roomRepository.findById(roomId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST_ROOM));

		// 2) 이미지 테이블에서 이 방에 속한 모든 이미지 조회
		List<String> imageUrls = imageRepository
			.findByOwnerTypeAndOwnerIdOrderBySortOrderAsc(
				OwnerType.ROOM, roomId
			)
			.stream()
			.map(Image::getUrl)
			.toList();

		// 3) DTO 빌드
		return RoomResponseDTO.GetRoomResponse.builder()
			.id(room.getId())
			.name(room.getName())
			.checkin(room.getCheckin())
			.checkout(room.getCheckout())
			.content(room.getContents())
			.price(room.getPrice())
			.imageUrls(imageUrls)               // ★ 단일 imageUrl → imageUrls
			.standardCount(room.getStandardCount())
			.totalCount(room.getTotalCount())
			.build();
	}

	public List<RoomResponseDTO.RoomSummary> getRoomsByGuesthouse(Long guesthouseId, LocalDate startDate,
		LocalDate endDate) {

		List<Room> rooms = roomRepository.findAllByGuesthouseId(guesthouseId);
		if (rooms.isEmpty())
			return List.of();

		// 1) 모든 방 ID 수집
		List<Long> roomIds = rooms.stream().map(Room::getId).toList();

		// 2) 방 이미지 일괄 조회 (ownerId, sortOrder 로 정렬됨)
		List<Image> roomImages = imageRepository
			.findByOwnerTypeAndOwnerIdInOrderByOwnerIdAscSortOrderAsc(OwnerType.ROOM, roomIds);

		// 3) 각 방의 "첫 번째" 이미지 URL만 맵에 저장 (이미 정렬되어 있으므로 putIfAbsent로 최초 1개만)
		Map<Long, String> firstImageByRoom = new LinkedHashMap<>();
		for (Image img : roomImages) {
			firstImageByRoom.putIfAbsent(img.getOwnerId(), img.getUrl());
		}

		return rooms.stream()
			.map(room -> {
				boolean isReserved = reservationRepository.existsOverlappingReservation(room, startDate, endDate);
				String ImageUrl = firstImageByRoom.getOrDefault(room.getId(), null);
				return roomConverter.toRoomSummary(room, !isReserved, ImageUrl);
			})
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<LocalDate> getFullyBookedDatesOfGuesthouse(Long guesthouseId) {
		LocalDate start = LocalDate.now();
		LocalDate endExclusive = start.plusMonths(3);

		long totalRooms = roomRepository.countByGuesthouseId(guesthouseId);
		if (totalRooms == 0) return List.of();

		List<Reservation> reservations =
				reservationRepository.findActivePaidByGuesthouseAndOverlap(guesthouseId, start, endExclusive);

		Map<LocalDate, Integer> occupiedCountByDate = new HashMap<>();
		for (Reservation r : reservations) {
			LocalDate s = r.getStartDate().isBefore(start) ? start : r.getStartDate();
			LocalDate e = r.getEndDate().isAfter(endExclusive) ? endExclusive : r.getEndDate();

			for (LocalDate d = s; d.isBefore(e); d = d.plusDays(1)) {
				occupiedCountByDate.merge(d, 1, Integer::sum);
			}
		}

		List<LocalDate> fullyBooked = new ArrayList<>();
		for (LocalDate d = start; d.isBefore(endExclusive); d = d.plusDays(1)) {
			if (occupiedCountByDate.getOrDefault(d, 0) >= totalRooms) {
				fullyBooked.add(d);
			}
		}
		return fullyBooked;
	}

	@Override
	@Transactional
	public List<LocalDate> getUnavailableDatesOfRoom(Long roomId) {
		Room room = roomRepository.findById(roomId)
				.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST_ROOM));

		LocalDate start = LocalDate.now();
		LocalDate endExclusive = start.plusMonths(3);

		List<Reservation> reservations =
				reservationRepository.findActivePaidByRoomAndOverlap(room, start, endExclusive);

		Set<LocalDate> unavailable = new LinkedHashSet<>();
		for (Reservation r : reservations) {
			LocalDate s = r.getStartDate().isBefore(start) ? start : r.getStartDate();
			LocalDate e = r.getEndDate().isAfter(endExclusive) ? endExclusive : r.getEndDate();

			for (LocalDate d = s; d.isBefore(e); d = d.plusDays(1)) {
				unavailable.add(d);
			}
		}
		return new ArrayList<>(unavailable);
	}
}
