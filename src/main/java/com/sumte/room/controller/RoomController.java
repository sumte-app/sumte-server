package com.sumte.room.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ApiResponse<Long> registerRoom(
		@PathVariable Long guesthouseId,
		@RequestBody @Valid RoomRequestDTO.RegisterRoom dto) {
		RoomResponseDTO.Registered room = roomCommandService.registerRoom(dto, guesthouseId);
		Long roomId = room.getRoomId();
		return ApiResponse.created(roomId);
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
	public ApiResponse<Long> updateRoom(
		@PathVariable Long guesthouseId, @PathVariable Long roomId,
		@RequestBody @Valid RoomRequestDTO.UpdateRoom dto
	) {
		roomCommandService.updateRoom(dto, guesthouseId, roomId);
		return ApiResponse.success(roomId);
	}

	@GetMapping("/room/{roomId}")
	@Operation(summary = "방 조회", description = "id기반으로 room을 조회하는 api입니다")
	@Parameters({
		@Parameter(name = "roomId", description = "방 아이디를 넘겨주세요.")
	})
	public ResponseEntity<ApiResponse<RoomResponseDTO.GetRoomResponse>> getRoom(
		@PathVariable Long roomId
	) {
		RoomResponseDTO.GetRoomResponse result = roomQueryService.getRoomById(roomId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	@GetMapping("/guesthouse/{guesthouseId}/rooms")
	// @Operation(summary = "특정 게스트하우스의 객실 목록 조회", description = "선택한 날짜 기준 예약 가능한 객실만 필터링하거나 전체 보여줄 수 있습니다.")
	@Operation(
		summary = "특정 게스트하우스의 객실 목록 조회", description = "선택한 날짜 기준으로 예약 가능한 객실만 필터링하거나 전체를 조회할 수 있습니다.\n각 방마다 예약 가능 여부가 포함되어 응답됩니다."
	)
	public ApiResponse<List<RoomResponseDTO.RoomSummary>> getRoomsByGuesthouse(
		@PathVariable Long guesthouseId,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
	) {
		return ApiResponse.success(roomQueryService.getRoomsByGuesthouse(guesthouseId, startDate, endDate));
	}

}
