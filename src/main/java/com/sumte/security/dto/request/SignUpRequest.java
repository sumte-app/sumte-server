package com.sumte.security.dto.request;

import java.time.LocalDate;

import com.sumte.user.entity.Gender;
import com.sumte.user.entity.User;
import com.sumte.user.entity.UserStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {

	private String loginId;
	private String password;
	private String name;
	private String nickname;
	private String phoneNumber;
	private Gender gender;
	private LocalDate birthday;
	private String email;

	public User toEntity(String encodedPassword) {
		return User.builder()
			.loginId(loginId)
			.password(encodedPassword)
			.name(name)
			.nickname(nickname)
			.phoneNumber(phoneNumber)
			.gender(gender)
			.birthday(birthday)
			.email(email)
			.status(UserStatus.ACTIVE)
			.build();
	}
}
