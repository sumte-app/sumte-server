package com.sumte.room.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

	@Override
	public List<RoomResponseDTO.RoomSummary> getRoomsByGuesthouseId(Long guesthouseId) {
		List<Room> rooms = roomRepository.findAllByGuesthouseId(guesthouseId);
		return rooms.stream()
			.map(roomConverter::toRoomSummary)
			.toList();
	}
}
