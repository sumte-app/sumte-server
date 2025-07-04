package com.sumte.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 찜 목록 조회 시 반환을 위한 dto (게하id, 이름, 찜 여부)
@Getter
@AllArgsConstructor
public class FavoriteResponseDto {
	private Long guesthouseId;   // 찜한 게스트하우스 ID
	private String guesthouseName; // 이름
	private boolean isFavorite;  // 지금 이 숙소가 찜 상태인지 (일단 추가)
}
