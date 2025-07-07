package com.sumte.room.service;

import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;

public interface RoomCommandService {
	RoomResponseDTO.Register registerRoom(RoomRequestDTO.Register dto, Long guesthouseId);

	RoomResponseDTO.Update updateRoom(RoomRequestDTO.Update dto, Long guesthouseId, Long roomId);

	RoomResponseDTO.Delete deleteRoom(Long roomId, Long guesthouseId);
}
