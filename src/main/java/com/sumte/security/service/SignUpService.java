package com.sumte.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.SecurityErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.security.dto.request.SignUpRequest;
import com.sumte.user.entity.User;
import com.sumte.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class SignUpService {

	private final UserRepository userRepository;
	public final PasswordEncoder passwordEncoder;

	public void signUpUser(SignUpRequest signUpRequest) {
		checkDuplicatedLoginId(signUpRequest.getLoginId());
		checkDuplicatedNickname(signUpRequest.getNickname());

		User user = signUpRequest.toEntity(passwordEncoder.encode(signUpRequest.getPassword()));
		userRepository.save(user);
	}

	public void checkDuplicatedLoginId(String loginId) {
		if (userRepository.findByLoginId(loginId).isPresent()) {
			throw new SumteException(SecurityErrorCode.IDENTIFIER_DUPLICATED);
		}
	}

	public void checkDuplicatedNickname(String nickname) {
		if (userRepository.findByNickname(nickname).isPresent()) {
			throw new SumteException(SecurityErrorCode.NICKNAME_DUPLICATED);
		}
	}

	public void checkDuplicatedEmail(String email) {
		if (userRepository.findByEmail(email).isPresent()) {
			throw new SumteException(SecurityErrorCode.EMAIL_DUPLICATED);
		}
	}
}
