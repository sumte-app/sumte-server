package com.sumte.room.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.room.dto.RoomRequestDTO;
import com.sumte.room.dto.RoomResponseDTO;
import com.sumte.room.service.RoomCommandService;

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

	@PostMapping("/{guesthouseId}/room")
	@Operation(summary = "방 추가 api", description = "숙소에 방을 추가하는 api 입니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "숙소 아이디를 넘겨주세요")
	})
	public ApiResponse<RoomResponseDTO.Register> registerRoom(
		@PathVariable Long guesthouseId,
		@RequestBody @Valid RoomRequestDTO.Register dto) {
		RoomResponseDTO.Register result = roomCommandService.registerRoom(dto, guesthouseId);

		return ApiResponse.success(result);

	}

	@DeleteMapping("/{guesthouseId}/room/{roomId}")
	@Operation(summary = "방 삭제 api", description = "숙소에 방을 삭제하는 api 입니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "숙소 아이디를 넘겨주세요"),
		@Parameter(name = "roomId", description = "방 아이디를 넘겨주세요.")
	})
	public ApiResponse<RoomResponseDTO.Delete> deleteRoom(
		@PathVariable Long guesthouseId, @PathVariable Long roomId
	) {
		RoomResponseDTO.Delete result = roomCommandService.deleteRoom(roomId, guesthouseId);

		return ApiResponse.success(result);

	}

}
