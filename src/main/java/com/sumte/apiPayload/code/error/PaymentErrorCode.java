package com.sumte.apiPayload.code.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements ErrorCode{

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_001", "존재하지 않는 예약입니다."),
    INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "PAYMENT_002", "유효하지 않은 결제 수단입니다."),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_003", "결제를 찾을 수 없습니다."),
    ALREADY_APPROVED_PAYMENT(HttpStatus.BAD_REQUEST, "PAYMENT_004", "이미 승인된 결제입니다."),
    PG_TOKEN_MISSING(HttpStatus.BAD_REQUEST, "PAYMENT_005", "pgToken이 누락되어 결제 승인을 처리할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
