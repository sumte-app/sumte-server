package com.sumte.room.service;

import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;

public interface RoomCommandService {
	RoomResponseDTO.Registered registerRoom(RoomRequestDTO.RegisterRoom dto, Long guesthouseId);

	RoomResponseDTO.Updated updateRoom(RoomRequestDTO.UpdateRoom dto, Long guesthouseId, Long roomId);

	RoomResponseDTO.Deleted deleteRoom(Long roomId, Long guesthouseId);
}
