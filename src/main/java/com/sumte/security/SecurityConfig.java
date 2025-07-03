package com.sumte.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sumte.security.filter.JwtAuthenticationFilter;
import com.sumte.security.tokenManger.TokenManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final TokenManager tokenManager;

	@Bean
	public SecurityFilterChain publicResourceConfig(HttpSecurity http) throws Exception {
		http.formLogin(FormLoginConfigurer::disable);
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(
			cors -> cors.configurationSource(corsConfigurationSource())
		);
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // 세션 사용 안함
		http.addFilterAt(new JwtAuthenticationFilter(tokenManager), BasicAuthenticationFilter.class);
		http.authorizeHttpRequests(
			(authorizeRequests)
				-> authorizeRequests.anyRequest().permitAll());
		return http.build();
	}

	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedOriginPattern("*");
		// configuration.addAllowedOrigin("http://localhost:5173");

		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");

		configuration.setAllowCredentials(false);

		// ***응답 헤더 노출***
		configuration.setExposedHeaders(List.of("Authorization", "Refresh")); // 필요하면 추가

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
