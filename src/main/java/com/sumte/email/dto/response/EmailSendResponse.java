package com.sumte.email.dto.response;

public record EmailSendResponse(
	boolean success,
	String message,
	long cooldownRemainingSeconds
) {
}