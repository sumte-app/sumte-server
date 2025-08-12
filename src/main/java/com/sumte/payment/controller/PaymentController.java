package com.sumte.payment.controller;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.payment.dto.KakaoPayApproveResponseDTO;
import com.sumte.payment.dto.PaymentRequestDTO;
import com.sumte.payment.dto.PaymentResponseDTO;
import com.sumte.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결재 관련 API (요청,승인 등)")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/request")
    @Operation(summary = "결제 요청 API",  description = "요청 본문으로 예약 ID, 결제 금액, 결제 수단을 입력받고, 결제창 URL과 결제 ID를 응답합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = PaymentResponseDTO.CreatePaymentDTO.class)
            )
    )
    public ResponseEntity<ApiResponse<PaymentResponseDTO.CreatePaymentDTO>> requestPayment(
            @Valid @RequestBody PaymentRequestDTO.CreatePaymentDTO dto) {

        PaymentResponseDTO.CreatePaymentDTO response = paymentService.requestPayment(dto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/approve")
    @Operation(
            summary = "결제 승인 처리 API",
            description = "PG사(예: 카카오페이) 결제 완료 후, 해당 결제 ID의 상태를 PAID로 변경합니다."
    )
    public ResponseEntity<ApiResponse<KakaoPayApproveResponseDTO>> approvePayment(
            @PathVariable("id") Long id,
            @RequestParam("pg_token") String pgToken) {

        KakaoPayApproveResponseDTO response = paymentService.approvePayment(id, pgToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
