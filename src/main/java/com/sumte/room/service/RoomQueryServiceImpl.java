package com.sumte.room.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sumte.reservation.repository.ReservationRepository;
import com.sumte.room.converter.RoomConverter;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomQueryServiceImpl implements RoomQueryService {

	private final RoomRepository roomRepository;
	private final RoomConverter roomConverter;
	private final ReservationRepository reservationRepository;

	@Override
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
