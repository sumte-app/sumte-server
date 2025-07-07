package com.sumte.guesthouse.repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sumte.guesthouse.dto.GuesthousePreviewDTO;
import com.sumte.guesthouse.dto.GuesthouseSearchRequestDTO;
import com.sumte.guesthouse.entity.QGuesthouse;
import com.sumte.guesthouse.entity.QOptionServices;
import com.sumte.guesthouse.entity.QTargetAudience;
import com.sumte.guesthouse.entity.mapping.QGuesthouseOptionServices;
import com.sumte.guesthouse.entity.mapping.QGuesthouseTargetAudience;
import com.sumte.reservation.entity.QReservation;
import com.sumte.review.entity.QReview;
import com.sumte.room.entity.QRoom;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GuesthouseRepositoryImpl implements GuesthouseRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Page<GuesthousePreviewDTO> searchFiltered(GuesthouseSearchRequestDTO dto, Pageable pageable) {

		QGuesthouse guesthouse = QGuesthouse.guesthouse;
		QRoom room = QRoom.room;
		QReservation reservation = QReservation.reservation;
		QReview review = QReview.review;
		QGuesthouseOptionServices guesthouseOptionServices = QGuesthouseOptionServices.guesthouseOptionServices;
		QGuesthouseTargetAudience guesthouseTargetAudience = QGuesthouseTargetAudience.guesthouseTargetAudience;
		QOptionServices optionService = QOptionServices.optionServices;
		QTargetAudience targetAudience = QTargetAudience.targetAudience;

		// 고정 조건들은 BooleanExpression으로 처리하는 게 좋다고 합니다.
		// 동적 조건은 BooleanBuilder를 사용하여 처리
		BooleanBuilder condition = new BooleanBuilder();

		BooleanBuilder roomFilter = new BooleanBuilder();

		// 1차 필터링 -> 인원, 날짜
		// 예약 날짜와 겹치는 예약 데이터를 가진 room을 거르기
		// 2차 필터링 -> 인원, 날짜, 키워드
		// StringUtils.hasText() : 공백만 있는 문자열, null값을 전부 false로 처리해줌.
		if (StringUtils.hasText(dto.getKeyword())) {
			String[] keywords = dto.getKeyword().split("\\s+");
			BooleanBuilder keywordCondition = new BooleanBuilder();
			for (String word : keywords) {
				BooleanExpression name = guesthouse.name.containsIgnoreCase(word);
				BooleanExpression region = guesthouse.addressRegion.containsIgnoreCase(word);
				keywordCondition.or(name).or(region);
			}

			condition.and(keywordCondition);
		}

		// 3차 필터링 -> 가격대, 객실정원, 부가서비스, 이용대상, 지역 설정

		if (dto.getMinPrice() != null && dto.getMaxPrice() != null) {
			roomFilter.and(room.price.between(dto.getMinPrice(), dto.getMaxPrice()));
		}

		if (dto.getMinPeople() != null) {
			roomFilter.and(room.standardCount.goe(dto.getMinPeople()));
		}

		if (dto.getMaxPeople() != null) {
			roomFilter.and(room.totalCount.loe(dto.getMaxPeople()));
		}

		if (dto.getRegion() != null && !dto.getRegion().isEmpty()) {
			condition.and(guesthouse.addressRegion.in(dto.getRegion()));
		}

		if (dto.getOptionService() != null && !dto.getOptionService().isEmpty()) {
			condition.and(guesthouse.id.in(
				JPAExpressions.select(guesthouseOptionServices.guesthouse.id)
					.from(guesthouseOptionServices)
					.join(guesthouseOptionServices.optionServices, optionService)
					.where(optionService.name.in(dto.getOptionService()))
			));
		}

		// 입력 인원이 최대 인원보다 작아야 하는 조건
		roomFilter.and(room.totalCount.goe(dto.getPeople()));

		// 예약 일자 필터링
		roomFilter.and(room.id.notIn(
			JPAExpressions.select(reservation.room.id)
				.from(reservation)
				.where(reservation.startDate.before(dto.getCheckOut()) // 사용자가 원하는 체크아웃 보다 전에 시작되고
					.and(reservation.endDate.after(dto.getCheckIn())) // 사용자가 원하는 체크인 보다 나중에 끝나는 예약이 있는 room.id를 거르기
				)
		));

		// List<Long> availabeReservationRoomIds = queryFactory
		// 	.select(room.id)
		// 	.from(room)
		// 	.where(roomFilter)
		// 	.fetch();

		if (dto.getTargetAudience() != null && !dto.getTargetAudience().isEmpty()) {
			condition.and(guesthouse.id.in(
				JPAExpressions.select(guesthouseTargetAudience.guesthouse.id)
					.from(guesthouseTargetAudience)
					.join(guesthouseTargetAudience.guesthouse, guesthouse)
					.where(targetAudience.name.in(dto.getTargetAudience()))
			));
		}

		// 리뷰 부분 필드만 따로 조회
		// 리스트 순회보다 서브쿼리가 더 효율적이라 하여
		List<Tuple> reviewStatics = queryFactory.select(
				room.guesthouse.id,
				review.score.avg(),
				review.id.count()
			).from(review).join(review.room, room)
			.where(room.id.in(
				JPAExpressions
					.select(room.id)
					.from(room)
					.where(roomFilter)
			))
			.fetch();

		Map<Long, Tuple> reviewStatMap = reviewStatics.stream()
			.collect(Collectors.toMap(
				tuple -> tuple.get(room.guesthouse.id),
				Function.identity()
			));

		List<GuesthousePreviewDTO> guesthouses = queryFactory
			.select(Projections.constructor(
				GuesthousePreviewDTO.class,
				guesthouse.id,
				guesthouse.name,
				Expressions.constant(0.0),       // 평균 점수는 나중에 덮어씀
				Expressions.constant(0L),        // 리뷰 수는 나중에 덮어씀
				room.price.min(),
				guesthouse.addressRegion,
				room.checkin.min()
			))
			.from(guesthouse)
			.join(guesthouse.rooms, room)
			.where(condition)
			.groupBy(guesthouse.id)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		guesthouses.forEach(preview -> {
			Tuple stats = reviewStatMap.get(preview.getId());
			if (stats != null) {
				Double avg = stats.get(review.score.avg());
				Long count = stats.get(review.id.count());
				preview.setAverageScore(avg != null ? avg : 0.0);
				preview.setReviewCount(count != null ? count : 0L);
			} else {
				preview.setAverageScore(0.0);
				preview.setReviewCount(0L);
			}
		});

		return new PageImpl<>(guesthouses, pageable, guesthouses.size());

	}
}
