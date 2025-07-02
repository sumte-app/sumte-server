package com.sumte.user.converter;

import org.springframework.stereotype.Component;

import com.sumte.user.dto.response.UserInfoResponse;
import com.sumte.user.entity.User;

@Component
public class UserConverter {

	public UserInfoResponse convertToUserInfoResponse(User user) {
		return new UserInfoResponse(user.getLoginId(), user.getName(), user.getNickname(),
			user.getPhoneNumber(), user.getGender(), user.getBirthday(), user.getEmail(), user.getStatus(),
			user.getInactiveDate());
	}
}
