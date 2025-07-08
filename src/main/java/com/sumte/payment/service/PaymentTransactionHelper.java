package com.sumte.payment.service;

import com.sumte.payment.entity.Payment;
import com.sumte.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

@Component
@RequiredArgsConstructor
public class PaymentTransactionHelper {

    private final PaymentRepository paymentRepository;

    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markPaymentFailed(Payment payment) {
        payment.markAsFailed();
        paymentRepository.save(payment);
    }
}