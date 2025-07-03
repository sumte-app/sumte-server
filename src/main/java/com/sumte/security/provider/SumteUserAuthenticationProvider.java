package com.sumte.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sumte.apiPayload.code.error.AuthorityErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.security.authenticationToken.SumteAuthenticationToken;
import com.sumte.security.service.SumteUserDetailService;
import com.sumte.security.userDetail.SumteUserDetails;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SumteUserAuthenticationProvider implements AuthenticationProvider {

	private final SumteUserDetailService sumteUserDetailService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String loginId = authentication.getName();
		String password = (String)authentication.getCredentials();

		UserDetails userDetails = sumteUserDetailService.loadUserByUsername(loginId);

		if (passwordEncoder.matches(password, userDetails.getPassword())) {
			return new SumteAuthenticationToken(
				userDetails,
				// principal은 UserDetails 객체, 기존에는 userDetails.getUsername()로 string으로 받음
				null,
				// 인증 후 credentials는 null, 기존에는 userDetails.getPassword()로 비밀번호 유지 인증 후 credentials는 null
				userDetails.getAuthorities(),
				((SumteUserDetails)userDetails).getDatabaseId()); // PK를 getDetails()로 받기 위해 따로 만든 메소드이므로 다운캐스팅
		} else {
			throw new SumteException(AuthorityErrorCode.PASSWORD_NOT_MATCH);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return SumteAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
