package com.sumte.apiPayload.handler;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sumte.apiPayload.ApiResponse;
import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.code.error.ErrorCode;
import com.sumte.apiPayload.exception.SumteException;

import lombok.extern.slf4j.Slf4j;

/**
 * ExceptionAdvice
 * 모든 예외 처리가 구현되지 않았습니다
 * 테스트하시다가 공통 api 응답 처리가 되지 않으면 추가 부탁드립니다
 */

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(SumteException.class)
	public ResponseEntity<Object> handleRestApiException(SumteException ex) {
		ErrorCode errorCode = ex.getErrorCode();
		return handleExceptionInternal(errorCode);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex) {
		log.warn("handleIllegalArgument");
		ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(errorCode, ex.getMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
		log.warn("handleConstraintViolationException");

		String message = ex.getConstraintViolations()
				.stream()
				.findFirst()
				.map(violation -> violation.getMessage())
				.orElse(CommonErrorCode.INVALID_PARAMETER.getMessage());

		return handleExceptionInternal(CommonErrorCode.INVALID_PARAMETER, message);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn("handleHttpRequestMethodNotSupportedException");
		ErrorCode errorCode = CommonErrorCode.METHOD_NOT_ALLOWED;
		return handleExceptionInternal(errorCode);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers,
		HttpStatusCode status, WebRequest request) {

		log.warn("MethodArgumentNotValidException ");
		ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(errorCode, getDefaultMessage(e));
	}

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
		HttpStatusCode status, WebRequest request) {

		// 상세 메시지 구성 : 내부 cause가 InvalidFormatException 인 경우(@RequestBody + Jackson이 DTO로 바꾸려다가 실패할 때)
		Throwable cause = ex.getCause();
		if (cause instanceof InvalidFormatException) {
			InvalidFormatException ife = (InvalidFormatException)cause;
			String fieldName = ife.getPath().get(0).getFieldName();
			String value = ife.getValue().toString();
			String targetType = ife.getTargetType().getSimpleName();

			String message = String.format("'%s'는 %s 필드에 유효하지 않은 값입니다. (%s 타입)", value, fieldName, targetType);

			ErrorCode errorCode = CommonErrorCode.INVALID_BODY;
			return handleExceptionInternal(errorCode, message);
		}

		ErrorCode errorCode = CommonErrorCode.INVALID_BODY;
		return handleExceptionInternal(errorCode, ex.getMessage());
	}

	private static String getDefaultMessage(MethodArgumentNotValidException e) {
		StringBuilder message = new StringBuilder();
		for (ObjectError error : e.getBindingResult().getAllErrors()) {
			message.append(error.getDefaultMessage()).append("\u00a0");
		}
		return message.toString();
	}

	private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.createFail(errorCode));
	}

	private ResponseEntity<Object> handleExceptionInternal(final ErrorCode errorCode, final String message) {
		return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.createFail(errorCode, message));
	}
}
