package com.sumte.guesthouse.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.service.GuesthouseCommandService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "게스트 하우스 관련 api", description = "게스트하우스 생성/수정/삭제 api 입니다.")
@RequiredArgsConstructor
@RequestMapping("/guesthouse")
public class GuesthouseController {
	private final GuesthouseCommandService guesthouseCommandService;

	@Operation(summary = "게스트하우스 등록", description = "게스트하우스를 등록합니다.")
	@PostMapping
	public ApiResponse<Void> registerGuesthouse(
		@RequestBody @Valid GuesthouseRequestDTO.Register dto) {
		guesthouseCommandService.registerGuesthouse(dto);

		return ApiResponse.successWithNoData();
	}

	@DeleteMapping("/{guesthouseId}")
	@Operation(summary = "게스트하우스 삭제", description = "게스트하우스를 db에서 삭제합니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "게스트하우스 아이디를 넘겨주세요")
	})
	public ApiResponse<Void> deleteGuesthouse(
		@PathVariable(name = "guesthouseId") Long guesthouseId) {
		guesthouseCommandService.deleteGuesthouse(guesthouseId);

		return ApiResponse.successWithNoData();

	}

	@PatchMapping("/{guesthouseId}")
	@Operation(summary = "게스트하우스 수정", description = "게스트하우스를 수정합니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "게스트하우스 아이디를 넘겨주세요")
	})
	public ApiResponse<Void> updateGuesthouse(
		@PathVariable(name = "guesthouseId") Long guesthouseId,
		@RequestBody @Valid GuesthouseRequestDTO.Update dto) {
		guesthouseCommandService.updateGuesthouse(guesthouseId, dto);
		return ApiResponse.successWithNoData();

	}

}
