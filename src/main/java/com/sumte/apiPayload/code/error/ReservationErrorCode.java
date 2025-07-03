package com.sumte.apiPayload.code.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ReservationErrorCode implements ErrorCode{

    // 예약 관련
    RESERVATION_DATE_INVALID(HttpStatus.BAD_REQUEST, "RES4000", "체크인과 체크아웃 날짜가 올바르지 않습니다."),
    ROOM_CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "RES4001", "객실 최대 인원을 초과했습니다."),
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "RES4002", "존재하지 않는 객실입니다."),
    ALREADY_RESERVED(HttpStatus.CONFLICT, "RES4003", "해당 날짜에 이미 예약이 존재합니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "RES4005", "해당 예약을 찾을 수 없습니다"),
    ALREADY_CANCELED(HttpStatus.BAD_REQUEST, "RESERVATION_4006", "이미 취소된 예약입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
