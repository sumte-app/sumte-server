package com.sumte.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.apiPayload.ApiResponse;
import com.sumte.security.authorization.UserId;
import com.sumte.user.dto.response.UserInfoResponse;
import com.sumte.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "사용자 관련 API", description = "사용자 관련 API 입니다.")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	//유저 조회
	@GetMapping
	@Operation(summary = "유저 정보 조회 API", description = "특정 유저의 정보를 조회합니다.")
	public ResponseEntity<UserInfoResponse> getUserInfo(
		@UserId Long userId) {
		UserInfoResponse response = userService.getUserInfo(userId);
		return ResponseEntity.ok(response);
	}

	//유저 탈퇴(비활성화)
	@Operation(summary = "유저 비활성화 API", description = "유저 탈퇴 시 비활성화 처리를 합니다.")
	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deactivateUser(
		@UserId Long userId) {
		userService.deactivateUser(userId);
		return ResponseEntity.ok(ApiResponse.successWithNoData());
	}

	//닉네임 중복확인
	@PatchMapping("/nickname")
	@Operation(summary = "닉네임 수정 API", description = "사용자의 닉네임을 수정합니다.")
	public ResponseEntity<Void> updateNickname(
		@RequestBody @Valid com.sumte.user.dto.UserNicknameUpdateRequest request,
		@UserId Long userId
	) {
		userService.updateNickname(userId, request.nickname());
		return ResponseEntity.ok().build();
	}
}
