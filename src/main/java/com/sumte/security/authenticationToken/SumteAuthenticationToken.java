package com.sumte.security.authenticationToken;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class SumteAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private final Long id;

	public SumteAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
		id = null;
	}

	public SumteAuthenticationToken(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities, Long id) {
		super(principal, credentials, authorities);
		this.id = id;
	}

	@Override
	public Long getDetails() {
		return id;
	}
}
