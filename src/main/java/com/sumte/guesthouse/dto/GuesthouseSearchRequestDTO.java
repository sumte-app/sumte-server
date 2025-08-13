package com.sumte.guesthouse.dto;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "GuesthouseSearchRequest", description = "게스트하우스 필터 검색 요청 DTO")
public class GuesthouseSearchRequestDTO {

	@NotNull
	@Schema(description = "예약 가능 여부 (true: 예약 가능만 조회, false: 전체 조회)", example = "true", required = true)
	private Boolean viewEnableReservation;

	@NotNull
	@Schema(description = "체크인 날짜 (yyyy-MM-dd)", example = "2025-08-20", required = true)
	private LocalDate checkIn;

	@NotNull
	@Schema(description = "체크아웃 날짜 (yyyy-MM-dd)", example = "2025-08-22", required = true)
	private LocalDate checkOut;

	@NotNull
	@Schema(description = "인원 수", example = "2", required = true)
	private Long people;

	@Schema(description = "최소 가격(필터링 시 입력)", example = "30000", required = false)
	private Long minPrice;

	@Schema(description = "최대 가격(필터링 시 입력)", example = "150000", required = false)
	private Long maxPrice;

	@Schema(description = "최소 인원(필터링 시 입력)", example = "1", required = false)
	private Long minPeople;

	@Schema(description = "최대 인원(필터링 시 입력)", example = "4", required = false)
	private Long maxPeople;

	@Schema(description = "검색 키워드 입력 (예: 제주 애월 끌림 게스트하우스)", example = "제주 애월 끌림 게스트하우스", required = false)
	private String keyword;

	@Schema(description = "원하는 부가 서비스 목록", example = "[\"이벤트\"]", required = false)
	private List<String> optionService;

	@Schema(description = "원하는 이용 대상 목록", example = "[\"애견동반\"]", required = false)
	private List<String> targetAudience;

	@Schema(description = "지역 키워드 목록 (피그마에 정의된 키워드 그대로 입력)", example = "[\"제주도\",\"서귀포시\"]", required = false)
	private List<String> region;
}

