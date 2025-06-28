package com.sumte.guesthouse.service;

import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;

public interface GuesthouseCommandService {
	GuesthouseResponseDTO.register registerGuesthouse(GuesthouseRequestDTO.register dto);

	GuesthouseResponseDTO.delete deleteGuesthouse(Long guesthouseId);
}
