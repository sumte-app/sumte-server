package com.sumte.review.converter;

import com.sumte.reservation.entity.Reservation;
import com.sumte.review.dto.ReviewRequestDto;
import com.sumte.review.dto.ReviewResponseDto;
import com.sumte.review.entity.Review;
import com.sumte.room.entity.Room;
import com.sumte.user.entity.User;

public class ReviewConverter {

	public static Review toEntity(ReviewRequestDto dto, User user, Reservation reservation) {
		return Review.builder()
			.user(user)
			.room(reservation.getRoom())
				.reservation(reservation)
			.contents(dto.getContents())
			.score(dto.getScore())
			.build();
	}

	public static void updateEntity(Review review, ReviewRequestDto dto) {
		review.changeContents(dto.getContents());
		review.changeScore(dto.getScore());
	}

	public static ReviewResponseDto toDto(Review review) {
		return new ReviewResponseDto(
				review.getId(),
				review.getUser().getId(),
				review.getRoom().getId(),
				review.getReservation() != null ? review.getReservation().getId() : null,
				review.getContents(),
				review.getScore()
		);
	}
}