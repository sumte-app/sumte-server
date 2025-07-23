package com.sumte.guesthouse.service;

import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;

import jakarta.transaction.Transactional;

public interface GuesthouseCommandService {
	GuesthouseResponseDTO.Register registerGuesthouse(GuesthouseRequestDTO.Register dto);

	@Transactional
	GuesthouseResponseDTO.Update updateGuesthouse(Long id, GuesthouseRequestDTO.Update dto);

	GuesthouseResponseDTO.delete deleteGuesthouse(Long guesthouseId);

	@Transactional
	void activateAdvertisement(Long guesthouseId);

	@Transactional
	void deactivateAdvertisement(Long guesthouseId);
}
