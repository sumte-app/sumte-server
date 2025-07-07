package com.sumte.payment.service;

import com.sumte.apiPayload.code.error.PaymentErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.payment.converter.RefundConverter;
import com.sumte.payment.dto.RefundRequestDTO;
import com.sumte.payment.entity.Payment;
import com.sumte.payment.entity.Refund;
import com.sumte.payment.entity.RefundStatus;
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

        Refund refund = RefundConverter.toEntity(dto, payment);
        refundRepository.save(refund);
    }

    @Override
    @Transactional
    public void approveRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new SumteException(PaymentErrorCode.REFUND_NOT_FOUND));

        if (refund.getRefundStatus() != RefundStatus.PENDING) {
            throw new SumteException(PaymentErrorCode.ALREADY_PROCESSED_REFUND);
        }

        refund.markAsCompleted();
        refund.getPayment().markAsRefunded();

        // 환불 실패의 경우 카카오페이 API 연동 후 구현
        // refund.markAsFailed();
    }
}
