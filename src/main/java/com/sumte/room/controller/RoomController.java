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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "객실 API", description = "특정 게스트하우스의 객실을 추가/수정/삭제 하는 api입니다")
@RequestMapping("/guesthouse")
public class RoomController {
	private final RoomCommandService roomCommandService;
	private final RoomQueryService roomQueryService;

	@PostMapping("/{guesthouseId}/room")
	@Operation(summary = "방 추가 api", description = "숙소에 방을 추가하는 api 입니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "숙소 아이디를 넘겨주세요")
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		description = "등록할 방 정보를 입력해주세요.",
		required = true,
		content = @Content(
			schema = @Schema(implementation = RoomRequestDTO.RegisterRoom.class),
			examples = @ExampleObject(
				name = "Register Room Example",
				value = """
					{
					  "name": "오션뷰 더블룸",
					  "content": "바다 전망, 킹사이즈 침대, 무료 Wi-Fi",
					  "price": 120000,
					  "checkin": "15:00:00",
					  "checkout": "11:00:00",
					  "standardCount": 2,
					  "totalCount": 4
					}
					"""
			)
		)
	)
	public ApiResponse<Long> registerRoom(
		@PathVariable Long guesthouseId,
		@RequestBody @Valid RoomRequestDTO.RegisterRoom dto) {
		RoomResponseDTO.Registered room = roomCommandService.registerRoom(dto, guesthouseId);
		Long roomId = room.getRoomId();
		return ApiResponse.created(roomId);
	}

	@PatchMapping("/{guesthouseId}/room/{roomId}")
	@Operation(summary = "방 수정 api", description = "방을 수정하는 api 입니다.")
	@Parameters({
		@Parameter(name = "guesthouseId", description = "숙소 아이디를 넘겨주세요"),
		@Parameter(name = "roomId", description = "방 아이디를 넘겨주세요.")
	})
	@io.swagger.v3.oas.annotations.parameters.RequestBody(
		description = "수정할 방 정보를 입력해주세요.",
		required = true,
		content = @Content(
			schema = @Schema(implementation = RoomRequestDTO.UpdateRoom.class),
			examples = @ExampleObject(
				name = "Update Room Example",
				value = """
					{
					  "name": "마운틴뷰 트윈룸",
					  "content": "산 전망, 트윈 침대 2개, 무료 Wi-Fi",
					  "price": 90000,
					  "checkin": "14:00:00",
					  "checkout": "10:00:00",
					  "standardCount": 2,
					  "totalCount": 3
					}
					"""
			)
		)
	)
	public ApiResponse<Long> updateRoom(
		@PathVariable Long guesthouseId, @PathVariable Long roomId,
		@RequestBody @Valid RoomRequestDTO.UpdateRoom dto
	) {
		roomCommandService.updateRoom(dto, guesthouseId, roomId);
		return ApiResponse.success(roomId);
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
		@Parameter(description = "체크인 날짜", example = "2025-08-13")
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@Parameter(description = "체크아웃 날짜", example = "2025-08-14")
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
	) {
		return ApiResponse.success(roomQueryService.getRoomsByGuesthouse(guesthouseId, startDate, endDate));
	}

	// 1) 게스트하우스: 앞으로 3개월간 모든 객실 매진일
	@GetMapping("/{guesthouseId}/fully-booked-dates")
	@Operation(summary = "게스트하우스 3개월 매진일 조회",
			description = "현재 날짜부터 3개월 동안 해당 게스트하우스의 모든 객실이 점유된 날짜 목록을 반환합니다.")
	public ApiResponse<List<LocalDate>> getFullyBookedDatesOfGuesthouse(
			@PathVariable Long guesthouseId
	) {
		return ApiResponse.success(roomQueryService.getFullyBookedDatesOfGuesthouse(guesthouseId));
	}

	// 2) 객실: 앞으로 3개월간 예약 불가일
	@GetMapping("/room/{roomId}/unavailable-dates")
	@Operation(summary = "객실 3개월 예약 불가일 조회",
			description = "현재 날짜부터 3개월 동안 해당 객실이 예약 불가한 날짜 목록을 반환합니다.")
	public ApiResponse<List<LocalDate>> getUnavailableDatesOfRoom(
			@PathVariable Long roomId
	) {
		return ApiResponse.success(roomQueryService.getUnavailableDatesOfRoom(roomId));
	}

}
