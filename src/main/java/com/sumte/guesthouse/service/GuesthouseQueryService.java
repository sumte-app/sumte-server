package com.sumte.guesthouse.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.sumte.guesthouse.dto.GuesthouseResponseDTO;

public interface GuesthouseQueryService {
	Slice<GuesthouseResponseDTO.HomeSummary> getGuesthousesForHome(Pageable pageable);
}