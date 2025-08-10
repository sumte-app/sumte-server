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
	private Integer reviewCount;
	private Long lowerPrice;
	private String addressRegion;
	private LocalTime checkinTime;
	private String imageUrl;
}
