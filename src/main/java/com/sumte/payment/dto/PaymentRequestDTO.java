package com.sumte.payment.dto;

import com.sumte.payment.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreatePaymentDTO {
        private Long reservationId;
        private Long amount;
        private PaymentMethod paymentMethod;
    }
}
