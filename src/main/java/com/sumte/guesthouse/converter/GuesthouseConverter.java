package com.sumte.guesthouse.converter;

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
}
