package com.sumte.review.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumte.apiPayload.code.error.ReviewErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.review.converter.ReviewConverter;
import com.sumte.review.dto.ReviewRequestDto;
import com.sumte.review.dto.ReviewSearchDto;
import com.sumte.review.entity.Review;
import com.sumte.review.repository.ReviewRepository;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;
import com.sumte.user.entity.User;
import com.sumte.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final RoomRepository roomRepository;

	@Transactional
	public Long createReview(Long userId, ReviewRequestDto dto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new SumteException(ReviewErrorCode.USER_NOT_FOUND));
		Room room = roomRepository.findById(dto.getRoomId())
			.orElseThrow(() -> new SumteException(ReviewErrorCode.ROOM_NOT_FOUND));

		Review review = ReviewConverter.toEntity(dto, user, room);
		Review saved = reviewRepository.save(review);
		return saved.getId();
	}

	@Transactional
	public Void updateReview(Long userId, Long reviewId, ReviewRequestDto dto) {
		Optional<Review> opt = reviewRepository.findByIdAndUserId(reviewId, userId);
		if (opt.isPresent()) {
			Review review = opt.get();
			ReviewConverter.updateEntity(review, dto);
		}
		// reviewId 자체가 존재하지 않으면 NOT_FOUND로
		if (!reviewRepository.existsById(reviewId)) {
			throw new SumteException(ReviewErrorCode.REVIEW_NOT_FOUND);
		}
		// id는 있지만 userId가 다르면 UNAUTH로
		throw new SumteException(ReviewErrorCode.UNAUTHORIZED);
	}

	@Transactional
	public void deleteReview(Long userId, Long id) {
		Optional<Review> opt = reviewRepository.findByIdAndUserId(id, userId);
		if (opt.isPresent()) {
			reviewRepository.delete(opt.get());
			return;
		}
		if (!reviewRepository.existsById(id)) {
			throw new SumteException(ReviewErrorCode.REVIEW_NOT_FOUND);
		}
		throw new SumteException(ReviewErrorCode.UNAUTHORIZED);
	}

	private ReviewSearchDto toListDto(Review r) {
		return new ReviewSearchDto(
			r.getId(),
			r.getContents(),
			r.getScore(),
			r.getUser().getNickname(),
			r.getCreatedAt()
		);
	}

	@Transactional(readOnly = true)
	public Page<ReviewSearchDto> getReviewsByGuesthouse(Long guesthouseId, Pageable pageable) {
		return reviewRepository
			.findAllByRoomGuesthouseId(guesthouseId, pageable)
			.map(this::toListDto);
	}

	//리뷰테이블에서 userid와 일치하는 모든 리뷰 가져오기, 이 각 엔티티->dto로 변환
	@Transactional(readOnly = true)
	public Page<ReviewSearchDto> getMyReviews(Long userId, Pageable pageable) {
		return reviewRepository
			.findAllByUserId(userId, pageable)
			.map(this::toListDto);
	}

}
