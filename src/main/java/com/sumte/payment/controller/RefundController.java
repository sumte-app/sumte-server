package com.sumte.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.payment.dto.RefundRequestDTO;
import com.sumte.payment.service.RefundService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/refunds")
@RequiredArgsConstructor
@Tag(name = "환불 API", description = "환불 관련 API (요청,승인 등)")
public class RefundController {

	private final RefundService refundService;

	@PostMapping
	@Operation(summary = "환불 요청 API", description = "결제 ID와 환불 사유를 받아 환불을 생성합니다.")
	public ResponseEntity<ApiResponse<Void>> requestRefund(@Valid @RequestBody RefundRequestDTO.CreateRefundDTO dto) {
		refundService.requestRefund(dto);
		return ResponseEntity.ok(ApiResponse.success(null));
	}

	@PatchMapping("/{id}/approve")
	@Operation(summary = "환불 승인 처리 API", description = "환불 ID를 받아 해당 환불을 승인 처리하고, 결제 상태를 REFUNDED로 환불 상태를 COMPLETED로 변경합니다.")
	public ResponseEntity<ApiResponse<Void>> approveRefund(@PathVariable Long id) {
		refundService.approveRefund(id);
		return ResponseEntity.ok(ApiResponse.success(null));
	}
}
