package com.sumte.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sumte.jpa.BaseTimeEntity;
import com.sumte.security.authority.SumteAuthority;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String loginId;
	private String password;
	private String name;
	private String nickname;
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private LocalDate birthday;
	private String email;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

	private LocalDateTime inactiveDate;

	public SumteAuthority getAuthority() {
		return SumteAuthority.INDIVIDUAL;
	}

	public void deactivate() {
		this.status = UserStatus.INACTIVE;
		this.inactiveDate = LocalDateTime.now();
	}

	@Builder
	public User(String loginId, String password, String name, String nickname, String phoneNumber, Gender gender,
		LocalDate birthday, String email, UserStatus status) {
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.birthday = birthday;
		this.email = email;
		this.status = status;
	}
}
