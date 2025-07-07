package com.sumte.payment.service;

import com.sumte.payment.dto.PaymentRequestDTO;
import com.sumte.payment.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO.CreatePaymentDTO requestPayment(PaymentRequestDTO.CreatePaymentDTO dto);
    void approvePayment(Long paymentId, PaymentRequestDTO.ApprovePaymentDTO dto);
}
