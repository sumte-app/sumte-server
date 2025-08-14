package com.sumte.email;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.email-verification")
public class EmailVerificationProperties {
	private int codeLength = 6;          // 인증코드 자리수
	private int ttlSeconds = 600;        // 유효기간(초)
	private int resendCooldownSeconds = 60; // 재전송 쿨다운(초)
	private String from = "no-reply@sumte.com";

	public int getCodeLength() {
		return codeLength;
	}

	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}

	public int getTtlSeconds() {
		return ttlSeconds;
	}

	public void setTtlSeconds(int ttlSeconds) {
		this.ttlSeconds = ttlSeconds;
	}

	public int getResendCooldownSeconds() {
		return resendCooldownSeconds;
	}

	public void setResendCooldownSeconds(int resendCooldownSeconds) {
		this.resendCooldownSeconds = resendCooldownSeconds;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}