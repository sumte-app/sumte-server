package com.sumte.room.converter;

import org.springframework.stereotype.Component;

import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.entity.Room;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RoomConverter {

	public Room toRegisterEntity(RoomRequestDTO.RegisterRoom dto) {
		Room room = Room.createRoomEntity(dto);

		return room;
	}

	public RoomResponseDTO.Registered toRegisterDTO(Room room) {
		return RoomResponseDTO.Registered.builder()
			.name(room.getName())
			.roomId(room.getId())
			.build();
	}

	public RoomResponseDTO.Deleted toDeleteEntity(Room room) {
		return RoomResponseDTO.Deleted.builder()
			.name(room.getName())
			.build();
	}

	public RoomResponseDTO.Updated toUpdateEntity(Room room) {
		return RoomResponseDTO.Updated.builder()
			.roomId(room.getId())
			.build();
	}

}
