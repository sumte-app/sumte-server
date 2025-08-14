package com.sumte.email.dto.response;

public record EmailVerifyResponse(
	boolean success,
	String message
) {
}