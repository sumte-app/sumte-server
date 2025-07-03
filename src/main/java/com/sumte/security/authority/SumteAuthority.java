package com.sumte.security.authority;

import org.springframework.security.core.GrantedAuthority;

public enum SumteAuthority implements GrantedAuthority {
	INDIVIDUAL, COMPANY, ADMIN;

	@Override
	public String getAuthority() {
		return name();
	}
}
