package com.sumte.room.service;

import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.room.converter.RoomConverter;
import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;

import jakarta.transaction.Transactional;
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

	@Override
	@Transactional
	public RoomResponseDTO.Update updateRoom(RoomRequestDTO.Update dto, Long guesthouseId, Long roomId) {
		Room room = roomRepository.findById(roomId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST_ROOM));

		if (dto.getName() != null) {

			//만약 수정하고 싶은 방의 이름이 이미 존재하는 경우
			roomRepository.findRoomByGuesthouseIdAndName(guesthouseId, dto.getName())
				.ifPresent(room1 -> {
					throw new SumteException(CommonErrorCode.ALREADY_EXIST);
				});

			room.setName(dto.getName());

		}
		if (dto.getContent() != null) {
			room.setContents(dto.getContent());
		}
		if (dto.getPrice() != null) {
			room.setPrice(dto.getPrice());
		}
		if (dto.getCheckin() != null) {
			room.setCheckin(dto.getCheckin());
		}
		if (dto.getCheckout() != null) {
			room.setCheckout(dto.getCheckout());
		}
		if (dto.getStandartCount() != null) {
			room.setStandardCount(dto.getStandartCount());
		}
		if (dto.getTotalCount() != null) {
			room.setTotalCount(dto.getTotalCount());
		}
		if (dto.getImageUrl() != null) {
			room.setImageUrl(dto.getImageUrl());
		}

		return roomConverter.toUpdateEntity(room);

	}
}
