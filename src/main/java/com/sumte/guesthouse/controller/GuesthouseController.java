package com.sumte.guesthouse.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.apiPayload.exception.annotation.CheckPage;
import com.sumte.apiPayload.exception.annotation.CheckPageSize;
import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;
import com.sumte.guesthouse.service.GuesthouseCommandService;
import com.sumte.guesthouse.service.GuesthouseQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@Tag(name = "게스트 하우스 관련 api", description = "게스트하우스 생성/수정/조회/삭제 api 입니다.")
@RequiredArgsConstructor
@RequestMapping("/guesthouse")
public class GuesthouseController {
	private final GuesthouseCommandService guesthouseCommandService;
	private final GuesthouseQueryService guesthouseQueryService;

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

	@PostMapping("/search")
	@Operation(summary = "게스트 하우스 필터링 조회", description = "게스트 하우스를 검색 조건에 맞게 조회합니다.")
	@Parameters({
		@Parameter(name = "page", description = "몇 번째 페이지를 조회할지 입력해주세요(1 이상의 정수)"),
		@Parameter(name = "size", description = "한번에 몇개의 게스트 하우스를 조회할지 입력해주세요(1 이상의 정수) ")
	})
	public ResponseEntity<ApiResponse<Page<GuesthousePreviewDTO>>> searchGuesthouse(
		@RequestBody @Valid GuesthouseSearchRequestDTO dto,
		@CheckPage @RequestParam(name = "page", defaultValue = "1") int page,
		@CheckPageSize @RequestParam(name = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page - 1, size);
		Page<GuesthousePreviewDTO> result = guesthouseQueryService.getFilteredGuesthouse(dto, pageable);
		return ResponseEntity.ok(ApiResponse.success(result));

	}

}
