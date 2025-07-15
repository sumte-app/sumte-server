package com.sumte.guesthouse.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;
import com.sumte.guesthouse.repository.GuesthouseRepositoryCustom;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuesthouseQueryServiceImpl implements GuesthouseQueryService {
	private final GuesthouseRepositoryCustom guesthouseRepositoryCustom;

	@Override
	@Transactional
	public Page<GuesthousePreviewDTO> getFilteredGuesthouse(GuesthouseSearchRequestDTO dto, Pageable pageable) {

		return guesthouseRepositoryCustom.searchFiltered(dto, pageable);
	}

}
