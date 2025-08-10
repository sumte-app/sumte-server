package com.sumte.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.security.converter.SignInConverter;
import com.sumte.security.dto.request.SignInRequest;
import com.sumte.security.dto.response.SignInResponse;
import com.sumte.security.tokenManger.TokenManager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원가입/로그인 API", description = "회원가입/로그인 관련 API입니다.")
@RestController
@RequestMapping("/users/login")
@RequiredArgsConstructor
public class SignInController {

	private final AuthenticationManager authenticationManager;
	private final TokenManager tokenManager;

	@Operation(summary = "로그인 API", description = "바디로 JWT 토큰을 반환합니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<SignInResponse>> singIn(@RequestBody SignInRequest signInRequest) {
		Authentication authentication = authenticationManager.authenticate(
			SignInConverter.toAuthenticationToken(signInRequest));
		String jwt = tokenManager.writeToken(authentication);
		return ResponseEntity.ok(ApiResponse.success(new SignInResponse("Bearer " + jwt)));
	}
}