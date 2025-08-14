package com.sumte.guesthouse.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.sumte.guesthouse.dto.GuesthouseDetailDTO;
import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;

public interface GuesthouseQueryService {
	GuesthouseDetailDTO getHouseById(Long id);

	Page<GuesthousePreviewDTO> getFilteredGuesthouse(GuesthouseSearchRequestDTO dto, Pageable pageable);

	Slice<GuesthouseResponseDTO.HomeSummary> getGuesthousesForHome(Pageable pageable);

	GuesthouseResponseDTO.HomeCard getGuesthouseSummary(Long guesthouseId);
}
