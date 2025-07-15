package com.sumte.guesthouse.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;

public interface GuesthouseQueryService {
	Page<GuesthousePreviewDTO> getFilteredGuesthouse(GuesthouseSearchRequestDTO dto, Pageable pageable);

}
