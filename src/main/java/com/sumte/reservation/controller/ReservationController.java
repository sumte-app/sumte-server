package com.sumte.reservation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.reservation.dto.ReservationRequestDTO;
import com.sumte.reservation.dto.ReservationResponseDTO;
import com.sumte.reservation.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Tag(name = "Reservation", description = "예약 관련 API (생성, 조회, 수정, 삭제 등)")
public class ReservationController {

	private final ReservationService reservationService;

	@PostMapping
	@Operation(summary = "예약 생성 API", description = "요청 본문으로 객실 ID, 투숙 인원, 날짜 정보를 입력받고, 헤더의 userId를 통해 예약을 생성합니다.")
	public ReservationResponseDTO.CreateReservationDTO createReservation(
		@Parameter(description = "요청한 사용자 ID") // 로그인 로직이 구현이 안되어 임시로 헤더로 해결
		@RequestHeader("userId") Long userId,
		@Parameter(description = "예약 생성 요청 DTO", required = true)
		@Valid @RequestBody ReservationRequestDTO.CreateReservationDTO request
	) {
		return reservationService.createReservation(request, userId);
	}
}
