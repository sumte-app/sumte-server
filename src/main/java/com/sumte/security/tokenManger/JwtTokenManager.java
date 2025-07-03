package com.sumte.security.tokenManger;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.sumte.security.authenticationToken.SumteAuthenticationToken;
import com.sumte.security.authority.SumteAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtTokenManager implements TokenManager {

	private final SecretKey secretKey;

	public JwtTokenManager(@Value("${jwt.secret}") String secretKey) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public SumteAuthenticationToken readToken(String token) throws
		SignatureException, UnsupportedJwtException, MalformedJwtException, ExpiredJwtException {

		Claims claims = Jwts.parser()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token)
			.getBody();

		String loginId = claims.get("loginId", String.class); // 로그인 시 사용하는 id값
		Long id = claims.get("id", Long.class); // 데이터베이스에서 사용되는 유저의 id값
		List<String> authorities = claims.get("authorities", List.class);

		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (String authority : authorities) {
			try {
				grantedAuthorities.add(SumteAuthority.valueOf(authority));
			} catch (IllegalArgumentException e) {
				throw new MalformedJwtException("Sumte에 등록되지 않은 권한이 포함되어 있습니다.");
			}
		}

		if (grantedAuthorities.isEmpty()) {
			throw new MalformedJwtException("권한 정보가 없습니다.");
		}

		// 권한이 여러 개인 경우도 모두 추가됨
		return new SumteAuthenticationToken(loginId, null, grantedAuthorities, id);
	}

	@Override
	public String writeToken(Authentication authentication) {
		return Jwts.builder()
			.setHeader(Map.of(
				"provider", "sumte",
				"type", "accessToken"
			))
			.setClaims(Map.of(
				"loginId", authentication.getName(),
				// 기존 .getPrincipal()로 UserDetails 전체가 직렬화됨(다만 principle을 userDetails.getUsername() 넣었어서 string으로 문제가 없었음 -> username(String)만
				"authorities",
				authentication.getAuthorities().stream() // GrantedAuthority 객체 리스트 직렬화 문제 -> List<String>
					.map(GrantedAuthority::getAuthority)
					.toList(),
				"id", authentication.getDetails()
			))
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // ms 단위, 24시간
			.signWith(secretKey)
			.compact();
	}
}
