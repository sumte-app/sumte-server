package com.sumte.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReviewResponseDto {
	private Long id;
	private Long userId;
	private Long roomId;
	private Long reservationId;
	private String contents;
	private int score;

}
