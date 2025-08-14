package com.sumte.guesthouse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.guesthouse.dto.OptionResponseDTO;
import com.sumte.guesthouse.service.TargetQeuryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "게스트 하우스 API", description = "게스트하우스 생성/수정/조회/삭제 api 입니다.")
@RequiredArgsConstructor
@RequestMapping("/target")
public class TargetAudienceController {

	private final TargetQeuryService targetQeuryService;

	@GetMapping
	@Operation(summary = "이용대상 조회", description = "필터링 시 게스트 하우스의 이용대상 목록을 전체 조회합니다")
	public ApiResponse<OptionResponseDTO.TargetList> getTargetAudience() {
		OptionResponseDTO.TargetList targetAudienceList = targetQeuryService.getAllTargetAudience();

		return ApiResponse.success(targetAudienceList);

	}
}
