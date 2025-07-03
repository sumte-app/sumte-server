package com.sumte.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

import com.sumte.security.provider.SumteUserAuthenticationProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig {

	private final SumteUserAuthenticationProvider sumteUserAuthenticationProvider;

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		List<AuthenticationProvider> providerList = new ArrayList<>();
		providerList.add(sumteUserAuthenticationProvider);
		return new ProviderManager(providerList);
	}
}

