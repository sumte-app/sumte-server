package com.sumte.guesthouse.service;

import com.sumte.guesthouse.dto.GuesthouseResponseDTO;

public interface GuesthouseQueryService {
	GuesthouseResponseDTO.GetHouseResponse getHouseById(Long id);
}
