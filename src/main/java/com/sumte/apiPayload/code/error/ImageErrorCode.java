package com.sumte.apiPayload.code.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
	IMAGE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "IMAGE400", "해당 이미지가 이미 등록되어 있습니다."),

	GUESTHOUSE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE404", "해당 게스트하우스를 찾을 수 없습니다."),

	ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE404", "해당 룸을 찾을 수 없습니다."),

	REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE404", "해당 리뷰를 찾을 수 없습니다."),

	INVALID_OWNER_TYPE(HttpStatus.BAD_REQUEST, "IMAGE400", "지원하지 않는 이미지 소유자 타입입니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}