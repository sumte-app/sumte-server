package com.sumte.security.filter;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sumte.security.authenticationToken.SumteAuthenticationToken;
import com.sumte.security.tokenManger.TokenManager;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenManager tokenManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String jwt = request.getHeader("Authorization");
		if (jwt == null) {
			filterChain.doFilter(request, response);
		} else {
			jwt = parseJwt(jwt);
			try {
				SumteAuthenticationToken authenticationToken = tokenManager.readToken(jwt);
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				filterChain.doFilter(request, response);
			}
			/*
			 * JWT 토큰 서명이 올바르지 않은 경우
			 * Jwt 토큰의 서명이 없는 경우
			 *  JWT 토큰이 만료된 경우
			 */ catch (SignatureException | UnsupportedJwtException | MalformedJwtException | ExpiredJwtException e) {
				filterChain.doFilter(request, response);
			}
		}
	}

	private String parseJwt(String jwt) {
		return jwt.replace("Bearer ", "");
	}
}
