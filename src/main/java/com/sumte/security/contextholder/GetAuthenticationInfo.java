package com.sumte.security.contextholder;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sumte.apiPayload.code.error.AuthorityErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.security.authority.SumteAuthority;

public class GetAuthenticationInfo {

	public static Long getUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (Long)authentication.getDetails();
	}

	public static SumteAuthority getAuthority() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getAuthorities().stream()
			.map(SumteAuthority.class::cast)
			.findFirst()
			.orElseThrow(() -> new SumteException(AuthorityErrorCode.AUTHORITY_ERROR_CODE));
	}
}
