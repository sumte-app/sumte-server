package com.sumte.email;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sumte.email.store.EmailVerificationStore;

@Component
@EnableScheduling
public class EmailVerificationCleaner {

	private final EmailVerificationStore store;

	public EmailVerificationCleaner(EmailVerificationStore store) {
		this.store = store;
	}

	// 1분마다 만료된 인증 항목 정리
	@Scheduled(fixedDelay = 60_000L)
	public void purge() {
		store.purgeExpired();
	}
}
