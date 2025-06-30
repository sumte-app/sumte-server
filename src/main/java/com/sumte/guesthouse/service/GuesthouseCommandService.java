package com.sumte.guesthouse.service;

import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;

public interface GuesthouseCommandService {
	GuesthouseResponseDTO.Register registerGuesthouse(GuesthouseRequestDTO.Register dto);

	GuesthouseResponseDTO.Update updateGuesthouse(Long id, GuesthouseRequestDTO.Update dto);

	GuesthouseResponseDTO.delete deleteGuesthouse(Long guesthouseId);
}
