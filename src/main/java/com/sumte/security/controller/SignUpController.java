package com.sumte.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.security.dto.request.SignUpRequest;
import com.sumte.security.service.SignUpService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원가입 API", description = "회원가입 API입니다.")
@RestController
@RequestMapping("/users/signup")
@RequiredArgsConstructor
public class SignUpController {

	private final SignUpService signUpService;

	@Operation(summary = "회원가입 API", description = "회원가입 API입니다.")
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> signUpPersonal(
		@RequestBody SignUpRequest signUpRequest) {
		signUpService.signUpUser(signUpRequest);
		return ResponseEntity.ok()
			.body(ApiResponse.createdWithNoData());
	}

	@Operation(summary = "아이디 중복 확인 API", description = "아이디 중복 확인 API입니다.")
	@GetMapping("/duplicate/id")
	public ResponseEntity<ApiResponse<String>> checkDuplicateLoginId(@RequestParam("id") String id) {
		signUpService.checkDuplicatedLoginId(id);
		return ResponseEntity.ok(ApiResponse.success("사용 가능한 아이디입니다."));
	}

	@Operation(summary = "닉네임 중복 확인 API", description = "닉네임 중복 확인 API입니다.")
	@GetMapping("/duplicate/nickname")
	public ResponseEntity<ApiResponse<String>> checkDuplicateNickname(@RequestParam("nickname") String nickname) {
		signUpService.checkDuplicatedNickname(nickname);
		return ResponseEntity.ok(ApiResponse.success("사용 가능한 닉네임입니다."));
	}

	@Operation(summary = "이메일 중복 확인 API", description = "이메일 중복 확인 API입니다.")
	@GetMapping("/duplicate/email")
	public ResponseEntity<ApiResponse<String>> checkDuplicateEmail(@RequestParam("email") String email) {
		signUpService.checkDuplicatedEmail(email);
		return ResponseEntity.ok(ApiResponse.success("사용 가능한 이메일입니다."));
	}

}
