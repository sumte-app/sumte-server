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
import com.querydsl.core.types.SubQueryExpression;
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
import com.sumte.reservation.converter.ReservationConverter;
import com.sumte.reservation.entity.QReservation;
import com.sumte.reservation.entity.ReservationStatus;
import com.sumte.review.entity.QReview;
import com.sumte.room.entity.QRoom;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class GuesthouseRepositoryImpl implements GuesthouseRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	private final ReservationConverter reservationConverter;

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
		BooleanBuilder roomFilter = new BooleanBuilder();
		BooleanBuilder condition = new BooleanBuilder();

		// StringUtils.hasText() : 공백만 있는 문자열, null값을 전부 false로 처리해줌.

		if (dto.getViewEnableReservation()) {
			SubQueryExpression<Long> reservedPeopleSum = JPAExpressions
				.select(reservation.adultCount.add(reservation.childCount).sum().coalesce(0L))
				.from(reservation)
				.where(
					reservation.room.id.eq(room.id),
					reservation.reservationStatus.ne(ReservationStatus.CANCELED),
					reservation.startDate.before(dto.getCheckOut()),
					reservation.endDate.after(dto.getCheckIn())
				);

			roomFilter.and(room.totalCount.subtract(reservedPeopleSum).goe(dto.getPeople()));
		}

		if (dto.getMinPrice() != null && dto.getMaxPrice() != null) {
			roomFilter.and(room.price.between(dto.getMinPrice(), dto.getMaxPrice()));
		}

		if (dto.getMinPeople() != null) {
			roomFilter.and(room.standardCount.goe(dto.getMinPeople()));
		}

		if (dto.getMaxPeople() != null) {
			roomFilter.and(room.totalCount.lt(dto.getMaxPeople()));
		}

		// roomFilter조건에 맞는 room.id 를 통해 guesthouse.id 를 추린다.
		List<Long> validRoomIds = queryFactory
			.select(room.id)
			.from(room)
			.where(roomFilter)
			.fetch();

		List<Long> validGuesthouseIds = queryFactory.select(room.guesthouse.id)
			.from(room)
			.where(room.id.in(validRoomIds))
			.distinct()
			.fetch();

		// guesthouse 조건 마저 설정하기
		condition.and(guesthouse.id.in(validGuesthouseIds));

		if (StringUtils.hasText(dto.getKeyword())) {
			String[] kewords = dto.getKeyword().split("\\s+");
			BooleanBuilder keywordCondition = new BooleanBuilder();

			for (String word : kewords) {
				keywordCondition.or(guesthouse.name.containsIgnoreCase(word));
				keywordCondition.or(guesthouse.addressRegion.containsIgnoreCase(word));
			}
			condition.and(keywordCondition);
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

		if (dto.getTargetAudience() != null && !dto.getTargetAudience().isEmpty()) {
			condition.and(guesthouse.id.in(
				JPAExpressions.select(guesthouseTargetAudience.guesthouse.id)
					.from(guesthouseTargetAudience)
					.join(guesthouseTargetAudience.targetAudience, targetAudience)
					.where(targetAudience.name.in(dto.getTargetAudience()))
			));
		}

		// 리뷰 통계 조회
		Map<Long, Tuple> reviewStatMap = queryFactory.select(
				room.guesthouse.id,
				review.score.avg(),
				review.id.count()
			).from(review)
			.join(review.room, room)
			.where(room.id.in(validRoomIds))
			.groupBy(room.guesthouse.id)
			.fetch()
			.stream()
			.collect(Collectors.toMap(
				t -> t.get(room.guesthouse.id),
				Function.identity()
			));

		// Step 5. guesthouse 리스트 조회 (room도 조인하여 최소 가격 등 포함)
		List<GuesthousePreviewDTO> guesthouses = queryFactory
			.select(Projections.constructor(
				GuesthousePreviewDTO.class,
				guesthouse.id,
				guesthouse.name,
				Expressions.constant(0.0),
				Expressions.constant(0L),
				room.price.min(),
				guesthouse.addressRegion,
				room.checkin.min()
			))
			.from(guesthouse)
			.join(guesthouse.rooms, room)
			.where(condition)
			.groupBy(guesthouse.id)
			.orderBy(review.id.count().desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		// Step 6. 리뷰 평균/갯수 덮어쓰기
		guesthouses.forEach(preview -> {
			Tuple stats = reviewStatMap.get(preview.getId());
			if (stats != null) {
				Double avg = stats.get(0, Double.class);
				Long count = stats.get(1, Long.class);
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
