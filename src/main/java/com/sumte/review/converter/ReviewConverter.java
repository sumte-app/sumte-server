package com.sumte.review.converter;

import com.sumte.review.dto.ReviewRequestDto;
import com.sumte.review.dto.ReviewResponseDto;
import com.sumte.review.entity.Review;
import com.sumte.room.entity.Room;
import com.sumte.user.entity.User;

public class ReviewConverter {

	public static Review toEntity(ReviewRequestDto dto, User user, Room room) {
		return Review.builder()
			.user(user)
			.room(room)
			.imageUrl(dto.getImageUrl())
			.contents(dto.getContents())
			.score(dto.getScore())
			.build();
	}

	public static void updateEntity(Review review, ReviewRequestDto dto) {
		review.changeImageUrl(dto.getImageUrl());
		review.changeContents(dto.getContents());
		review.changeScore(dto.getScore());
	}

	public static ReviewResponseDto toDto(Review review) {
		return new ReviewResponseDto(
			review.getId(),
			review.getUser().getId(),
			review.getRoom().getId(),
			review.getImageUrl(),
			review.getContents(),
			review.getScore()
		);
	}
}