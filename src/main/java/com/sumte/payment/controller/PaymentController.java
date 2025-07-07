package com.sumte.payment.controller;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.payment.dto.PaymentRequestDTO;
import com.sumte.payment.dto.PaymentResponseDTO;
import com.sumte.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "결재 관련 API (요청,승인 등)")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/request")
    @Operation(summary = "결제 요청 API",  description = "요청 본문으로 예약 ID, 결제 금액, 결제 수단을 입력받고, 결제창 URL과 결제 ID를 응답합니다.")
    public ResponseEntity<ApiResponse<PaymentResponseDTO.CreatePaymentDTO>> requestPayment(
            @Valid @RequestBody PaymentRequestDTO.CreatePaymentDTO dto) {

        PaymentResponseDTO.CreatePaymentDTO response = paymentService.requestPayment(dto);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
