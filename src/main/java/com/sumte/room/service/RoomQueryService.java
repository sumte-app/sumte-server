package com.sumte.room.service;

import java.util.List;

import com.sumte.room.dto.RoomResponseDTO;

public interface RoomQueryService {
	List<RoomResponseDTO.RoomSummary> getRoomsByGuesthouseId(Long guesthouseId);
}
