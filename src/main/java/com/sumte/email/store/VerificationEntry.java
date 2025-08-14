package com.sumte.email.store;

import java.time.Instant;

public record VerificationEntry(
	String code,
	Instant expiresAt,
	Instant lastSentAt
) {
	public boolean isExpired(Instant now) {
		return expiresAt.isBefore(now);
	}
}
