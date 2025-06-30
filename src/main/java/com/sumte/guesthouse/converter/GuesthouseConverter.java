package com.sumte.guesthouse.converter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
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
			.name(guesthouse.getName())
			.addressRegion(guesthouse.getAddressRegion())
			.build();

	}

	public GuesthouseResponseDTO.Update toUpdateResponseDTO(Guesthouse guesthouse, List<String> optionServices,
		List<String> targetAudience) {
		return GuesthouseResponseDTO.Update.builder()
			.name(guesthouse.getName())
			.addressRegion(guesthouse.getAddressRegion())
			.addressDetail(guesthouse.getAddressDetail())
			.targetAudience(targetAudience)
			.optionServices(optionServices)
			.build();
	}
}
