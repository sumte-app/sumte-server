package com.sumte.guesthouse.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GuesthousePreviewDTO {
	private Long id;
	private String name;
	private Double averageScore;
	private Long reviewCount;
	private Long lowerPrice;
	private String addressRegion;
	private LocalTime checkinTime;

	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}

	public void setReviewCount(Long reviewCount) {
		this.reviewCount = reviewCount;
	}
}
