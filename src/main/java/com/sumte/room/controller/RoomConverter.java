package com.sumte.room.controller;

import org.springframework.stereotype.Component;

import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.entity.Room;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomConverter {

	public Room toRegisterEntity(RoomRequestDTO.Register dto) {
		Room room = Room.createRoomEntity(dto);

		return room;
	}

	public RoomResponseDTO.Register toRegisterDTO(Room room) {
		return RoomResponseDTO.Register.builder()
			.name(room.getName())
			.roomId(room.getId())
			.build();
	}

	public RoomResponseDTO.Delete toDeleteEntity(Room room) {
		return RoomResponseDTO.Delete.builder()
			.name(room.getName())
			.build();
	}

}
