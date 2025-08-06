package com.sumte.guesthouse.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.converter.GuesthouseConverter;
import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.repository.GuesthouseOptionServicesRepository;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.guesthouse.repository.GuesthouseRepositoryCustom;
import com.sumte.guesthouse.repository.GuesthouseTargetAudienceRepository;
import com.sumte.review.repository.ReviewRepository;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.repository.RoomRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuesthouseQueryServiceImpl implements GuesthouseQueryService {

	private final GuesthouseRepository guesthouseRepository;
	private final RoomRepository roomRepository;
	private final ReviewRepository reviewRepository;
	private final GuesthouseConverter guesthouseConverter;
	private final GuesthouseTargetAudienceRepository guesthouseTargetAudienceRepository;
	private final GuesthouseOptionServicesRepository guesthouseOptionServicesRepository;
	private final GuesthouseRepositoryCustom guesthouseRepositoryCustom;

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
			.id(guesthouse.getId())
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

	@Override
	@Transactional
	public Slice<GuesthouseResponseDTO.HomeSummary> getGuesthousesForHome(Pageable pageable) {
		Slice<Guesthouse> guesthouses = guesthouseRepository.findAllOrderedForHome(pageable);

		return guesthouses.map(gh -> guesthouseConverter.toHomeSummary(
			gh,
			Optional.ofNullable(reviewRepository.findAverageScoreByGuesthouseId(gh.getId())).orElse(0.0),
			reviewRepository.countByGuesthouseId(gh.getId()),
			Optional.ofNullable(roomRepository.findEarliestCheckinByGuesthouseId(gh.getId())).orElse("00:00"),
			Optional.ofNullable(roomRepository.findMinPriceByGuesthouseId(gh.getId())).orElse(0L)
		));
	}

	@Override
	@Transactional
	public Page<GuesthousePreviewDTO> getFilteredGuesthouse(GuesthouseSearchRequestDTO dto, Pageable pageable) {
		return guesthouseRepositoryCustom.searchFiltered(dto, pageable);
	}
}