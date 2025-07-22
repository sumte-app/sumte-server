package com.sumte.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.user.converter.UserConverter;
import com.sumte.user.dto.response.UserInfoResponse;
import com.sumte.user.entity.User;
import com.sumte.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final UserConverter userConverter;

	//유저 조회(테스트용)
	@Transactional(readOnly = true)
	public UserInfoResponse getUserInfo(Long id) {
		User user = userRepository.findById(id).orElseThrow(() -> new SumteException(CommonErrorCode.USER_NOT_FOUND));

		return userConverter.convertToUserInfoResponse(user);
	}

	//유저 탈퇴(비활성화)
	@Transactional
	public void deactivateUser(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.USER_NOT_FOUND));

		user.deactivate();
	}

	// 닉네임 수정
	@Transactional
	public void updateNickname(Long userId, String nickname) {
		// 중복검사를 위함
		userRepository.findByNickname(nickname).ifPresent(user -> {
			throw new SumteException(CommonErrorCode.DUPLICATE_NICKNAME);
		});

		// 사용자 조회해서 닉네임 변경하기
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.USER_NOT_FOUND));

		user.updateNickname(nickname);
	}
}
