package com.sumte.guesthouse.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.entity.AdType;
import com.sumte.guesthouse.entity.Guesthouse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GuesthouseConverter {
	public Guesthouse toRegisterEntity(GuesthouseRequestDTO.Register dto) {
		return Guesthouse.createByRegisterDTO(dto);
	}

	public GuesthouseResponseDTO.Register toRegisterResponseDTO(Guesthouse guesthouse) {
		return GuesthouseResponseDTO.Register.builder()
			.id(guesthouse.getId())
			.name(guesthouse.getName())
			.build();

	}

	public GuesthouseResponseDTO.Update toUpdateResponseDTO(Guesthouse guesthouse, List<String> optionServices,
		List<String> targetAudience) {
		return GuesthouseResponseDTO.Update.builder()
			.id(guesthouse.getId())
			.name(guesthouse.getName())
			.addressRegion(guesthouse.getAddressRegion())
			.addressDetail(guesthouse.getAddressDetail())
			.targetAudience(targetAudience)
			.optionServices(optionServices)
			.build();
	}

	public GuesthouseResponseDTO.HomeSummary toHomeSummary(
		Guesthouse guesthouse,
		Double avgScore,
		int reviewCount,
		String checkInTime,
		Long minPrice
	) {
		return GuesthouseResponseDTO.HomeSummary.builder()
			.guestHouseId(guesthouse.getId())
			.name(guesthouse.getName())
			.addressRegion(guesthouse.getAddressRegion())
			.averageScore(avgScore)
			.reviewCount(reviewCount)
			.checkInTime(checkInTime)
			.minPrice(minPrice)
			.isAd(guesthouse.getAdvertisement() == AdType.AD)
			.build();
	}
}
