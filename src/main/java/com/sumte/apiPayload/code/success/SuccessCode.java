package com.sumte.apiPayload.code.success;

import org.springframework.http.HttpStatus;

public interface SuccessCode {

	HttpStatus getHttpStatus();

	String getCode();

	String getMessage();
}
