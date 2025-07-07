package com.sumte.guesthouse.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.sumte.guesthouse.converter.GuesthouseConverter;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.review.repository.ReviewRepository;
import com.sumte.room.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuesthouseQueryServiceImpl implements GuesthouseQueryService {

	private final GuesthouseRepository guesthouseRepository;
	private final RoomRepository roomRepository;
	private final ReviewRepository reviewRepository;
	private final GuesthouseConverter guesthouseConverter;

	@Override
	// 정렬된 게스트하우스 데이터를 조회해서 DTO로 변환 (리뷰, 체크인시간, 최소가격)
	public Slice<GuesthouseResponseDTO.HomeSummary> getGuesthousesForHome(Pageable pageable) {
		Slice<Guesthouse> guesthouses = guesthouseRepository.findAllOrderedForHome(pageable);

		return guesthouses.map(gh -> guesthouseConverter.toHomeSummary(
			gh,
			Optional.ofNullable(reviewRepository.findAverageScoreByGuesthouseId(gh.getId())).orElse(0.0),
			reviewRepository.countByGuesthouseId(gh.getId()),
			Optional.ofNullable(roomRepository.findEarliestCheckinByGuesthouseId(gh.getId())).orElse("00:00"),
			Optional.ofNullable(roomRepository.findMinPriceByGuesthouseId(gh.getId())).orElse(0L)
		));
	}
}