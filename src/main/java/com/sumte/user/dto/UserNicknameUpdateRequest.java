package com.sumte.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UserNicknameUpdateRequest(
	@NotBlank(message = "닉네임은 필수입니다.")
	String nickname
) {
}