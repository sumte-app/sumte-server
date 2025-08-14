package com.sumte.email.store;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class EmailVerificationStore {

	private final Map<String, VerificationEntry> store = new ConcurrentHashMap<>();

	public VerificationEntry get(String email) {
		return store.get(norm(email));
	}

	public void put(String email, VerificationEntry entry) {
		store.put(norm(email), entry);
	}

	public void remove(String email) {
		store.remove(norm(email));
	}

	public void purgeExpired() {
		Instant now = Instant.now();
		store.entrySet().removeIf(e -> e.getValue().isExpired(now));
	}

	private String norm(String email) {
		return email.trim().toLowerCase();
	}
}