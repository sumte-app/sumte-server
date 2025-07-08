package com.sumte.payment.converter;

import com.sumte.payment.dto.RefundRequestDTO;
import com.sumte.payment.entity.Payment;
import com.sumte.payment.entity.Refund;
import com.sumte.payment.entity.RefundStatus;

public class RefundConverter {

    public static Refund toEntity(RefundRequestDTO.CreateRefundDTO dto, Payment payment) {
        return Refund.builder()
                .payment(payment)
                .refundPrice(payment.getPaidPrice())
                .refundReason(dto.getRefundReason())
                .refundStatus(RefundStatus.PENDING)
                .build();
    }
}
