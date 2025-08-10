package com.sumte.reservation.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.sumte.reservation.dto.ReservationRequestDTO;
import com.sumte.reservation.dto.ReservationResponseDTO;
import com.sumte.reservation.service.ReservationService;
import com.sumte.security.authorization.UserId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Validated
@Tag(name = "예약 API", description = "예약 관련 API (생성, 조회, 수정, 삭제 등)")
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping
	@Operation(summary = "예약 생성 API", description = "요청 본문으로 객실 ID, 투숙 인원, 날짜 정보를 입력받고, 헤더의 userId를 통해 예약을 생성합니다.")
	public ResponseEntity<ApiResponse<Void>> createReservation(
			@Valid @RequestBody ReservationRequestDTO.CreateReservationDTO request
	) {
		reservationService.createReservation(request);
		return ResponseEntity.ok(ApiResponse.success(null));
	}

	@GetMapping("/my")
	@Operation(summary = "내 예약 목록 조회 API", description = "내가 예약한 숙소 목록을 페이지 단위로 조회합니다.")
	public ResponseEntity<ApiResponse<Page<ReservationResponseDTO.MyReservationDTO>>> getMyReservations(
			@CheckPage @RequestParam(name = "page", defaultValue = "1") int page,
			@CheckPageSize @RequestParam(name = "size", defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
		Page<ReservationResponseDTO.MyReservationDTO> result = reservationService.getMyReservations(pageable);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	@GetMapping("/{id}")
	@Operation(summary = "예약 상세 조회 API", description = "예약 ID를 기준으로 상세 정보를 조회합니다.")
	public ResponseEntity<ApiResponse<ReservationResponseDTO.ReservationDetailDTO>> getReservationDetail(
			@PathVariable("id") Long reservationId
	) {
		ReservationResponseDTO.ReservationDetailDTO result = reservationService.getReservationDetail(reservationId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}

	@PatchMapping("/{id}")
	@Operation(summary = "예약 취소 API", description = "예약 ID를 기준으로 사용자의 예약을 취소합니다.")
	public ResponseEntity<ApiResponse<Void>> cancelReservation(
			@PathVariable("id") Long reservationId
	) {
		reservationService.cancelReservation(reservationId);
		return ResponseEntity.ok(ApiResponse.success(null));
	}
}
