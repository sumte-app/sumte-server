package com.sumte.review.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//리뷰 목록 조회 시 반환할 DTO

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ReviewSearchDto {
	private Long id;
	private String imageUrl;
	private String contents;
	private int score;
	private String authorNickname;
	private LocalDateTime createdAt;  // 리뷰 조회시 작성일자가 보여야하므로
}