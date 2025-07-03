package com.sumte.security.tokenManger;

import org.springframework.security.core.Authentication;

import com.sumte.security.authenticationToken.SumteAuthenticationToken;

public interface TokenManager {
	SumteAuthenticationToken readToken(String token);

	String writeToken(Authentication authentication);
}
