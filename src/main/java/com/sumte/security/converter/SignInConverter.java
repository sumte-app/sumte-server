package com.sumte.security.converter;

import com.sumte.security.authenticationToken.SumteAuthenticationToken;
import com.sumte.security.dto.request.SignInRequest;

public class SignInConverter {

	public static SumteAuthenticationToken toAuthenticationToken(SignInRequest signInRequest) {
		return new SumteAuthenticationToken(signInRequest.getLoginId(), signInRequest.getPassword());
	}
}