package com.sumte.guesthouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.guesthouse.dto.OptionResponseDTO;
import com.sumte.guesthouse.service.OptionQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "부가서비스 조회 api", description = "부가서비스 리스트를 조회하는 api 입니다.")
@RequiredArgsConstructor
@RequestMapping("/option")
public class OptionServiceController {

	private final OptionQueryService optionQueryService;

	@GetMapping
	@Operation(summary = "부가서비스 목록 조회", description = "부가서비스 목록을 전체 조회합니다")
	public ApiResponse<OptionResponseDTO.OptionList> getOptionServices() {
		OptionResponseDTO.OptionList optionServiceList = optionQueryService.getAllOptionServices();

		return ApiResponse.success(optionServiceList);

	}
}
