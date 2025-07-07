package com.sumte.apiPayload.code.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PaymentErrorCode implements ErrorCode{

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_001", "존재하지 않는 예약입니다."),
    INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "PAYMENT_002", "유효하지 않은 결제 수단입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
