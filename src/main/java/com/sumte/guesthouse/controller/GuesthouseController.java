package com.sumte.guesthouse.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.service.GuesthouseCommandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guesthouse")
public class GuesthouseController {
	private final GuesthouseCommandService guesthouseCommandService;

	@PostMapping
	public ApiResponse<GuesthouseResponseDTO.register> registerGuesthouse(
		@RequestBody @Valid GuesthouseRequestDTO.register dto) {
		GuesthouseResponseDTO.register response = guesthouseCommandService.registerGuesthouse(dto);

		return ApiResponse.success(response);
	}

}
