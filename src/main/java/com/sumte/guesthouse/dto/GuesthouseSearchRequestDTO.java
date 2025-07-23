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
public class GuesthouseSearchRequestDTO {

	@NotNull
	private Boolean viewEnableReservation;

	@NotNull
	@Schema(description = "체크인 날짜 (yyyy-MM-dd)", required = true)
	private LocalDate checkIn;

	@NotNull
	@Schema(description = "체크아웃 날짜 (yyyy-MM-dd)", required = true)
	private LocalDate checkOut;

	@NotNull
	@Schema(description = "인원 수", required = true)
	private Long people;

	@Schema(description = "최소 가격(필터링 시 입력)", required = false)
	private Long minPrice;

	@Schema(description = "최대 가격(필터링 시 입력)", required = false)
	private Long maxPrice;

	@Schema(description = "최소 인원(필터링 시 입력)", required = false)
	private Long minPeople;

	@Schema(description = "최대 인원(필터링 시 입력)", required = false)
	private Long maxPeople;

	@Schema(description = "검색 키워드 입력 (제주 애월 끌림 게스트하우스)", required = false)
	private String keyword;

	@Schema(description = "원하는 부가 서비스를 조회후 해당 목록에 있는 값들 중 원하는 것들을 list형태로 입력", required = false)
	private List<String> optionService;

	@Schema(description = "원하는 이용대상을 조회후 해당 목록에 있는 값들 중 원하는 것들을 list형태로 입력", required = false)
	private List<String> targetAudience;

	@Schema(description = "지역 키워드를 선택하여 list형태로 입력 (피그마에 나와있는 키워드 그대로 입력) ", required = false)
	private List<String> region;

}

