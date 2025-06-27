package com.sumte.apiPayload.code.success;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonSuccessCode implements SuccessCode {

	SUCCESS(HttpStatus.OK, "COMMON200", "요청이 성공적으로 처리되었습니다."),
	CREATED(HttpStatus.CREATED, "COMMON201", "리소스가 성공적으로 생성되었습니다.");

	private final HttpStatus httpStatus;

	private final String code;

	private final String message;
}
