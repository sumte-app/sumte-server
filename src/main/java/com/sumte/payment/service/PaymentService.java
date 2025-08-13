package com.sumte.payment.service;

import com.sumte.payment.dto.KakaoPayApproveResponseDTO;
import com.sumte.payment.dto.PaymentRequestDTO;
import com.sumte.payment.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO.PaymentReadyResponse requestPayment(PaymentRequestDTO.PaymentRequestCreate dto);
    KakaoPayApproveResponseDTO approvePayment(Long paymentId, String pgToken);
}
