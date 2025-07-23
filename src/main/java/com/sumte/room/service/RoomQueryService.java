package com.sumte.room.service;

import com.sumte.room.dto.RoomResponseDTO;

public interface RoomQueryService {
	RoomResponseDTO.GetRoomResponse getRoomById(Long roomId);
}
