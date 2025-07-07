package com.sumte.room.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.service.RoomCommandService;
import com.sumte.room.service.RoomQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "Room API", description = "room을 추가/수정/삭제 하는 api입니다")
@RequestMapping("/guesthouse")
public class RoomController {
	private final RoomCommandService roomCommandService;
	private final RoomQueryService roomQueryService;

	@PostMapping("/{guesthouseId}/room")
	@Operation(summary = "방 추가 api", description = "숙소에 방을 추가하는 api 입니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "숙소 아이디를 넘겨주세요")
	})
	public ApiResponse<Void> registerRoom(
		@PathVariable Long guesthouseId,
		@RequestBody @Valid RoomRequestDTO.Register dto) {
		roomCommandService.registerRoom(dto, guesthouseId);

		return ApiResponse.successWithNoData();

	}

	@DeleteMapping("/{guesthouseId}/room/{roomId}")
	@Operation(summary = "방 삭제 api", description = "숙소에 방을 삭제하는 api 입니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "숙소 아이디를 넘겨주세요"),
		@Parameter(name = "roomId", description = "방 아이디를 넘겨주세요.")
	})
	public ApiResponse<Void> deleteRoom(
		@PathVariable Long guesthouseId, @PathVariable Long roomId
	) {
		roomCommandService.deleteRoom(roomId, guesthouseId);

		return ApiResponse.successWithNoData();

	}

	@PatchMapping("/{guesthouseId}/room/{roomId}")
	@Operation(summary = "방 수정 api", description = "방을 수정하는 api 입니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "숙소 아이디를 넘겨주세요"),
		@Parameter(name = "roomId", description = "방 아이디를 넘겨주세요.")
	})
	public ApiResponse<Void> updateRoom(
		@PathVariable Long guesthouseId, @PathVariable Long roomId,
		@RequestBody @Valid RoomRequestDTO.Update dto
	) {
		roomCommandService.updateRoom(dto, guesthouseId, roomId);
		return ApiResponse.successWithNoData();
	}

	@GetMapping("/{guesthouseId}/rooms")
	@Operation(summary = "특정 게스트하우스의 객실 목록 조회", description = "선택한 숙소에 등록된 객실 목록을 반환합니다.")
	@Parameter(name = "guesthouseId", description = "숙소 ID를 입력해주세요")
	public ApiResponse<List<RoomResponseDTO.RoomSummary>> getRoomsByGuesthouseId(
		@PathVariable Long guesthouseId
	) {
		return ApiResponse.success(roomQueryService.getRoomsByGuesthouseId(guesthouseId));
	}

}
