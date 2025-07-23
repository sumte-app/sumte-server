package com.sumte.room.service;

import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomQueryServiceImpl implements RoomQueryService {

	private final RoomRepository roomRepository;

	@Override
	@Transactional
	public RoomResponseDTO.GetRoomResponse getRoomById(Long roomId) {
		Room room = roomRepository.findById(roomId).orElseThrow(
			() -> new SumteException(CommonErrorCode.NOT_EXIST_ROOM)
		);

		return RoomResponseDTO.GetRoomResponse.builder()
			.name(room.getName())
			.checkin(room.getCheckin())
			.checkout(room.getCheckout())
			.content(room.getContents())
			.price(room.getPrice())
			.imageUrl(room.getImageUrl())
			.standardCount(room.getStandardCount())
			.totalCount(room.getTotalCount())
			.build();
	}

}
