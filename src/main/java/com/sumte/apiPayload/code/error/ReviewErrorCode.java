package com.sumte.apiPayload.code.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

	// REVIEW 전용 4XX 에러?
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW404", "리뷰 작성자를 찾을 수 없습니다."),
	ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW404", "리뷰 대상 방을 찾을 수 없습니다."),
	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW404", "해당 리뷰를 찾을 수 없습니다."),
	UNAUTHORIZED(HttpStatus.FORBIDDEN, "REVIEW403", "권한이 없습니다."),
	ALREADY_REVIEWED(HttpStatus.CONFLICT, "REVIEW409", "이미 해당 예약에 대한 후기가 작성되었습니다.");
	// FORBIDDEN_REVIEW(HttpStatus.FORBIDDEN, "REVIEW403", "본인 리뷰만 수정/삭제할 수 있습니다."); 일단 만들어주기 본인의 리뷰에 대해서만 처리

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}