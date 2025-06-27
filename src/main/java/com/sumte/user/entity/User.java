package com.sumte.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sumte.jpa.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
	private String userPassword;
	private String name;
	private String nickname;
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	private LocalDate birthday;
	private String email;
	private String address;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

	private LocalDateTime inactiveDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// public SumteAuthority getAuthority() {
	// 	return SumteAuthority.INDIVIDUAL;
	// }

	public void deactivate() {
		this.status = UserStatus.INACTIVE;
	}
}
