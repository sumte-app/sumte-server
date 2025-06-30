package com.sumte.reservation.controller;

import com.sumte.apiPayload.exception.annotation.CheckPage;
import com.sumte.apiPayload.exception.annotation.CheckPageSize;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.sumte.reservation.dto.ReservationRequestDTO;
import com.sumte.reservation.dto.ReservationResponseDTO;
import com.sumte.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Validated
@Tag(name = "Reservation", description = "예약 관련 API (생성, 조회, 수정, 삭제 등)")
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping
	@Operation(summary = "예약 생성 API", description = "요청 본문으로 객실 ID, 투숙 인원, 날짜 정보를 입력받고, 헤더의 userId를 통해 예약을 생성합니다.")
	public ReservationResponseDTO.CreateReservationDTO createReservation(
		@Parameter(description = "요청한 사용자 ID") // 로그인 로직이 구현이 안되어 임시로 헤더로 해결
		@RequestHeader("userId") Long userId,
		@Valid @RequestBody ReservationRequestDTO.CreateReservationDTO request
	) {
		return reservationService.createReservation(request, userId);
	}

	@GetMapping("/my")
	@Operation(summary = "내 예약 목록 조회 API", description = "내가 예약한 숙소 목록을 페이지 단위로 조회합니다.")
	public Page<ReservationResponseDTO.MyReservationDTO> getMyReservations(
			@Parameter(description = "요청한 사용자 ID") @RequestHeader("userId") Long userId,
			@CheckPage @RequestParam(name = "page", defaultValue = "1") int page,
			@CheckPageSize @RequestParam(name = "size", defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "startDate"));
		return reservationService.getMyReservations(userId, pageable);
	}

	@GetMapping("/{id}")
	@Operation(summary = "예약 상세 조회 API", description = "예약 ID를 기준으로 상세 정보를 조회합니다.")
	public ReservationResponseDTO.ReservationDetailDTO getReservationDetail(
			@Parameter(description = "요청한 사용자 ID") @RequestHeader("userId") Long userId,
			@PathVariable("id") Long reservationId
	) {
		return reservationService.getReservationDetail(reservationId, userId);
	}

}
