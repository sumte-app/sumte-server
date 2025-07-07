package com.sumte.apiPayload.code.error;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

	// COMMON 4XX
	INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON400", "파라미터가 올바르지 않습니다."),
	INVALID_BODY(HttpStatus.BAD_REQUEST, "COMMON400", "요청 본문이 올바르지 않습니다."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "찾을 수 없는 리소스입니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "사용자를 찾을 수 없습니다"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON405", "허용되지 않는 HTTP Method입니다."),

    // GUESTHOUSE4XX
    DUPLICATE_DATA(HttpStatus.BAD_REQUEST, "GUESTHOUSE401", "중복된 게스트하우스 입니다."),
    NOT_EXIST(HttpStatus.BAD_REQUEST, "GUESTHOUSE402", "존재하지 않는 게스트하우스 입니다."),
    OPTIONSERVICE_NOT_EXIST(HttpStatus.BAD_REQUEST, "GUESTHOUSE403", "존재하지 않는 부가 서비스 입니다."),
    TARGETAUDIENCE_NOT_EXIST(HttpStatus.BAD_REQUEST, "GUESTHOUSE404", "존재하지 않는 이용대상 입니다."),

    // ROOM4XX
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "ROOM401", "이미 같은 방이 존재합니다."),
    NOT_EXIST_ROOM(HttpStatus.BAD_REQUEST, "ROOM402", "존재하지 않는 방입니다."),

	// COMMON 5XX
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 내부 오류입니다."),

    // page 처리 관련
    PAGE_UNDER_ONE(HttpStatus.BAD_REQUEST,"PAGE_4001","페이지는 1이상으로 입력해야 합니다."),
    PAGE_SIZE_UNDER_ONE(HttpStatus.BAD_REQUEST,"PAGE_4002","페이지 사이즈는 1이상으로 입력해야 합니다.");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
