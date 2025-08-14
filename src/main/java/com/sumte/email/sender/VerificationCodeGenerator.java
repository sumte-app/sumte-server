package com.sumte.email.sender;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class VerificationCodeGenerator {
	private final SecureRandom random = new SecureRandom();

	public String numeric(int length) {
		StringBuilder sb = new StringBuilder(length);
		for (int i = 0; i < length; i++)
			sb.append(random.nextInt(10));
		return sb.toString();
	}
}
