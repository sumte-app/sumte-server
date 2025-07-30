package com.sumte.guesthouse.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;

public interface GuesthouseQueryService {
	GuesthouseResponseDTO.GetHouseResponse getHouseById(Long id);

	Page<GuesthousePreviewDTO> getFilteredGuesthouse(GuesthouseSearchRequestDTO dto, Pageable pageable);

}
