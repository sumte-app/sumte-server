package com.sumte.apiPayload.code.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FavoriteErrorCode implements ErrorCode {
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "FAVORITE404", "사용자를 찾을 수 없습니다."),
	GUESTHOUSE_NOT_FOUND(HttpStatus.NOT_FOUND, "FAVORITE404", "게스트하우스를 찾을 수 없습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 오류입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}