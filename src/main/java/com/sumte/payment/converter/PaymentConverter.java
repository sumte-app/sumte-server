package com.sumte.payment.converter;

import com.sumte.payment.dto.PaymentRequestDTO;
import com.sumte.payment.dto.PaymentResponseDTO;
import com.sumte.payment.entity.Payment;
import com.sumte.payment.entity.PaymentMethod;
import com.sumte.payment.entity.PaymentStatus;
import com.sumte.reservation.entity.Reservation;

public class PaymentConverter {

    public static Payment toEntity(PaymentRequestDTO.PaymentRequestCreate dto, Reservation reservation) {
        PaymentMethod method = dto.getPaymentMethod() != null
                ? dto.getPaymentMethod()
                : PaymentMethod.KAKAOPAY;

        return Payment.builder()
                .reservation(reservation)
                .paidPrice(dto.getAmount())
                .paymentMethod(method)
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }

    public static PaymentResponseDTO.PaymentReadyResponse toCreateResponse(Payment payment, String paymentUrl, String appScheme) {
        return PaymentResponseDTO.PaymentReadyResponse.builder()
                .paymentId(payment.getId())
                .paymentUrl(paymentUrl)
                .appScheme(appScheme)
                .build();
    }

}
