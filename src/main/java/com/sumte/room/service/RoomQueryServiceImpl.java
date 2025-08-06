package com.sumte.room.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
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

	@Override
	@Transactional
	public RoomResponseDTO.GetRoomResponse getRoomById(Long roomId) {
		Room room = roomRepository.findById(roomId).orElseThrow(
			() -> new SumteException(CommonErrorCode.NOT_EXIST_ROOM)
		);

		return RoomResponseDTO.GetRoomResponse.builder()
			.id(room.getId())
			.name(room.getName())
			.checkin(room.getCheckin())
			.checkout(room.getCheckout())
			.content(room.getContents())
			.price(room.getPrice())
			.imageUrl(room.getImageUrl())
			.standardCount(room.getStandardCount())
			.totalCount(room.getTotalCount())
			.build();
	public List<RoomResponseDTO.RoomSummary> getRoomsByGuesthouse(Long guesthouseId, LocalDate startDate,
		LocalDate endDate) {
		List<Room> rooms = roomRepository.findAllByGuesthouseId(guesthouseId);
		return rooms.stream()
			.map(room -> {
				boolean isReserved = reservationRepository.existsOverlappingReservation(room, startDate, endDate);
				return roomConverter.toRoomSummary(room, !isReserved);
			})
			.collect(Collectors.toList());
	}

}
