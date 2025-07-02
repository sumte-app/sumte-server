package com.sumte.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDto {
	
	@NotNull
	private Long roomId;

	private String imageUrl;

	@NotBlank
	private String contents;

	@Min(1)
	@Max(5)
	private int score;
}