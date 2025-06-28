package com.sumte.room.service;

import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;

public interface RoomCommandService {
	RoomResponseDTO.Register registerRoom(RoomRequestDTO.Register dto, Long guesthouseId);
}
