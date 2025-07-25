package com.sumte.payment.service;

import com.sumte.apiPayload.code.error.PaymentErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.payment.converter.PaymentConverter;
import com.sumte.payment.dto.PaymentRequestDTO;
import com.sumte.payment.dto.PaymentResponseDTO;
import com.sumte.payment.entity.Payment;
import com.sumte.payment.entity.PaymentStatus;
import com.sumte.payment.repository.PaymentRepository;
import com.sumte.reservation.entity.Reservation;
import com.sumte.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentTransactionHelper transactionHelper;

    @Override
    @Transactional
    public PaymentResponseDTO.CreatePaymentDTO requestPayment(PaymentRequestDTO.CreatePaymentDTO dto) {
        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new SumteException(PaymentErrorCode.RESERVATION_NOT_FOUND));

        Payment payment;
        try {
            payment = PaymentConverter.toEntity(dto, reservation);
        } catch (IllegalArgumentException e) {
            throw new SumteException(PaymentErrorCode.INVALID_PAYMENT_METHOD);
        }
        paymentRepository.save(payment);

        String paymentUrl = "https://pay.mock.kakao.com/" + payment.getId();
        return PaymentConverter.toCreateResponse(payment, paymentUrl);
    }

    @Override
    @Transactional
    public void approvePayment(Long paymentId, String pgToken) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new SumteException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new SumteException(PaymentErrorCode.ALREADY_APPROVED_PAYMENT);
        }

        if (pgToken == null || pgToken.isBlank()) {
            transactionHelper.markPaymentFailed(payment);
            throw new SumteException(PaymentErrorCode.PG_TOKEN_MISSING);
        }

        payment.markAsPaid();
    }

}
