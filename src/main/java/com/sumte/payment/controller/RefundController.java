package com.sumte.payment.controller;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.payment.dto.RefundRequestDTO;
import com.sumte.payment.service.RefundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refunds")
@RequiredArgsConstructor
@Tag(name = "Refund", description = "환불 관련 API (요청,승인 등)")
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    @Operation(summary = "환불 요청 API", description = "결제 ID와 환불 사유를 받아 환불을 생성하고, 결제 상태를 REFUNDED, 예약 상태를 CANCELED로 변경합니다.")
    public ResponseEntity<ApiResponse<Void>> requestRefund(@Valid @RequestBody RefundRequestDTO.CreateRefundDTO dto) {
        refundService.requestRefund(dto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
