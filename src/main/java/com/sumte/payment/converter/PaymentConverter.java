package com.sumte.payment.converter;

import com.sumte.payment.dto.PaymentRequestDTO;
import com.sumte.payment.dto.PaymentResponseDTO;
import com.sumte.payment.entity.Payment;
import com.sumte.payment.entity.PaymentMethod;
import com.sumte.payment.entity.PaymentStatus;
import com.sumte.reservation.entity.Reservation;

public class PaymentConverter {

    public static Payment toEntity(PaymentRequestDTO.CreatePaymentDTO dto, Reservation reservation) {
        return Payment.builder()
                .reservation(reservation)
                .paidPrice(dto.getAmount())
                .paymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod()))
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }

    public static PaymentResponseDTO.CreatePaymentDTO toCreateResponse(Payment payment, String paymentUrl) {
        return PaymentResponseDTO.CreatePaymentDTO.builder()
                .paymentId(payment.getId())
                .paymentUrl(paymentUrl)
                .build();
    }

}
