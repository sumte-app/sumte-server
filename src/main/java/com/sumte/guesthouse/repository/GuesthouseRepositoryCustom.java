package com.sumte.guesthouse.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;
import com.sumte.guesthouse.entity.Guesthouse;

public interface GuesthouseRepositoryCustom {
	Page<Guesthouse> searchFiltered(GuesthouseSearchRequestDTO dto, Pageable pageable);
}
