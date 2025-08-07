package com.sumte.guesthouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;

public interface GuesthouseRepositoryCustom {
	Page<GuesthousePreviewDTO> searchFiltered(GuesthouseSearchRequestDTO dto, Pageable pageable);
}
