package com.sumte.room.service;

import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.room.controller.RoomConverter;
import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomCommandServiceImpl implements RoomCommandService {

	private final RoomConverter roomConverter;
	private final RoomRepository roomRepository;
	private final GuesthouseRepository guesthouseRepository;

	@Override
	public RoomResponseDTO.Register registerRoom(RoomRequestDTO.Register dto, Long guesthouseId) {
		Room registerRoom = roomConverter.toRegisterEntity(dto);
		Guesthouse guesthouse = guesthouseRepository.findById(guesthouseId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST));

		registerRoom.setGuesthouse(guesthouse);

		// 해당 숙소에 같은 이름의 방이 존재하는지 확인
		roomRepository.findRoomByGuesthouseIdAndName(guesthouseId, dto.getName())
			.ifPresent(room -> {
				throw new SumteException(CommonErrorCode.ALREADY_EXIST);
			});

		roomRepository.save(registerRoom);
		RoomResponseDTO.Register result = roomConverter.toRegisterDTO(registerRoom);
		return result;
	}

	@Override
	public RoomResponseDTO.Delete deleteRoom(Long roomId, Long guesthouseId) {
		Room room = roomRepository.findById(roomId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST));

		roomRepository.delete(room);

		return roomConverter.toDeleteEntity(room);
	}
}
