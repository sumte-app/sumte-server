package com.sumte.payment.service;

import com.sumte.apiPayload.code.error.PaymentErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.payment.converter.RefundConverter;
import com.sumte.payment.dto.RefundRequestDTO;
import com.sumte.payment.entity.Payment;
import com.sumte.payment.entity.Refund;
import com.sumte.payment.repository.PaymentRepository;
import com.sumte.payment.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final PaymentRepository paymentRepository;
    private final RefundRepository refundRepository;

    @Override
    @Transactional
    public void requestRefund(RefundRequestDTO.CreateRefundDTO dto) {
        Payment payment = paymentRepository.findById(dto.getPaymentId())
                .orElseThrow(() -> new SumteException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        payment.markAsRefunded();

        Refund refund = RefundConverter.toEntity(dto, payment);
        refundRepository.save(refund);
    }
}
