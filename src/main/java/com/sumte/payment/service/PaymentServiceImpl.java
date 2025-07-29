package com.sumte.payment.service;

import com.sumte.apiPayload.code.error.PaymentErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.payment.converter.PaymentConverter;
import com.sumte.payment.dto.*;
import com.sumte.payment.entity.Payment;
import com.sumte.payment.entity.PaymentStatus;
import com.sumte.payment.kakaopay.KakaoPayClient;
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
    private final KakaoPayClient kakaoPayClient;

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

        KakaoPayReadyRequestDTO request = KakaoPayReadyRequestDTO.builder()
                .cid("TC0ONETIME")
                .partner_order_id("test_order1")
                .partner_user_id("test_user1")
                .item_name("테스트_숨터")
                .quantity("1")
                .total_amount("1000")
                .tax_free_amount("0")
                .approval_url("http://localhost:8080/pay/success")
                .cancel_url("http://localhost:8080/pay/cancel")
                .fail_url("http://localhost:8080/pay/fail")
                .build();

        KakaoPayReadyResponseDTO kakaoResponse = kakaoPayClient.requestPayment(request);
        payment.setTid(kakaoResponse.getTid());

        return PaymentConverter.toCreateResponse(payment, kakaoResponse.getNext_redirect_pc_url());
    }

    @Override
    @Transactional
    public KakaoPayApproveResponseDTO approvePayment(Long paymentId, String pgToken) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new SumteException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new SumteException(PaymentErrorCode.ALREADY_APPROVED_PAYMENT);
        }

        if (pgToken == null || pgToken.isBlank()) {
            transactionHelper.markPaymentFailed(payment);
            throw new SumteException(PaymentErrorCode.PG_TOKEN_MISSING);
        }

        KakaoPayApproveRequestDTO request = KakaoPayApproveRequestDTO.builder()
                .cid("TC0ONETIME")
                .tid(payment.getTid())
                .partner_order_id("test_order1")
                .partner_user_id("test_user1")
                .pg_token(pgToken)
                .build();

        KakaoPayApproveResponseDTO response = kakaoPayClient.approvePayment(request);
        payment.markAsPaid();

        return response;
    }
}
