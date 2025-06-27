package com.sumte.apiPayload.exception;

import com.sumte.apiPayload.code.error.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SumteException extends RuntimeException {

	private final ErrorCode errorCode;
}
