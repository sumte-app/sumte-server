package com.sumte.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
	private Long id;
	private Long userId;
	private Long roomId;
	private String imageUrl;
	private String contents;
	private int score;
	
}
