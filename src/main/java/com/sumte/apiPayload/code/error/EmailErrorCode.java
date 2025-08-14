package com.sumte.apiPayload.code.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailErrorCode implements ErrorCode {

	// 400 Bad Request
	VERIFICATION_CODE_NOT_FOUND("Email404", "인증코드가 만료되었거나 요청되지 않았습니다.", HttpStatus.BAD_REQUEST),
	VERIFICATION_CODE_MISMATCH("Email404", "인증코드가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
	MAIL_SENDING_FAILED("Email404", "메일 전송에 실패했습니다.", HttpStatus.BAD_REQUEST),
	EMAIL_ALREADY_VERIFIED("Email400", "이미 인증이 완료된 이메일입니다.", HttpStatus.BAD_REQUEST),
	RESEND_COOLDOWN("Email429", "재전송 쿨다운 중입니다.", HttpStatus.TOO_MANY_REQUESTS),
	INVALID_EMAIL_FORMAT("Email400", "올바르지 않은 이메일 형식입니다.", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;

	@Override
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
}