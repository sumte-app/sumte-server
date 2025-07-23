package com.sumte.guesthouse.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.repository.GuesthouseOptionServicesRepository;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.guesthouse.repository.GuesthouseTargetAudienceRepository;
import com.sumte.room.dto.RoomResponseDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuesthouseQueryServiceImpl implements GuesthouseQueryService {

	private final GuesthouseRepository guesthouseRepository;
	private final GuesthouseTargetAudienceRepository guesthouseTargetAudienceRepository;
	private final GuesthouseOptionServicesRepository guesthouseOptionServicesRepository;

	@Override
	@Transactional
	public GuesthouseResponseDTO.GetHouseResponse getHouseById(Long id) {

		Guesthouse guesthouse = guesthouseRepository.findById(id).orElseThrow(
			() -> new SumteException(CommonErrorCode.NOT_EXIST)
		);

		List<String> targetAudiences = guesthouseTargetAudienceRepository.findTargetAudienceNamesByGuesthouseId(id);
		List<String> optionServices = guesthouseOptionServicesRepository.findTargetAudienceNamesByGuesthouseId(id);

		List<RoomResponseDTO.GetRoomResponse> roomDtos = guesthouse.getRooms().stream()
			.map(room -> RoomResponseDTO.GetRoomResponse.builder()
				.id(room.getId())
				.name(room.getName())
				.content(room.getContents())
				.price(room.getPrice())
				.checkin(room.getCheckin())
				.checkout(room.getCheckout())
				.standardCount(room.getStandardCount())
				.totalCount(room.getTotalCount())
				.imageUrl(room.getImageUrl())
				.build())
			.collect(Collectors.toList());

		return GuesthouseResponseDTO.GetHouseResponse.builder()
			.name(guesthouse.getName())
			.addressDetail(guesthouse.getAddressDetail())
			.addressRegion(guesthouse.getAddressRegion())
			.information(guesthouse.getInformation())
			.imageUrl(guesthouse.getImageUrl())
			.targetAudience(targetAudiences)
			.optionServices(optionServices)
			.rooms(roomDtos)
			.build();
	}
}
