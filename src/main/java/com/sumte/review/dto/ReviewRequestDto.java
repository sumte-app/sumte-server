package com.sumte.review.dto;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReviewRequestDto {

	@NotNull
	private Long roomId;

	@NotBlank
	private String contents;

	@Min(1)
	@Max(5)
	private int score;

	private List<@NotBlank String> imageUrls;
}