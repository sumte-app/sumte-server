package com.sumte.room.service;

import java.time.LocalDate;
import java.util.List;

import com.sumte.room.dto.RoomResponseDTO;

public interface RoomQueryService {
	List<RoomResponseDTO.RoomSummary> getRoomsByGuesthouse(Long guesthouseId, LocalDate startDate, LocalDate endDate);
	RoomResponseDTO.GetRoomResponse getRoomById(Long roomId);
	List<LocalDate> getFullyBookedDatesOfGuesthouse(Long guesthouseId);
	List<LocalDate> getUnavailableDatesOfRoom(Long roomId);
}
