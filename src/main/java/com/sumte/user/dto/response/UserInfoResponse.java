package com.sumte.user.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sumte.user.entity.Gender;
import com.sumte.user.entity.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

	private String loginId;

	private String name;

	private String nickname;

	private String phoneNumber;

	private Gender gender;

	private LocalDate birthday;

	private String email;

	private UserStatus status;

	private LocalDateTime inActiveDate;
}
