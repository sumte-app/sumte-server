package com.sumte.review.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumte.apiPayload.code.error.ReviewErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.review.converter.ReviewConverter;
import com.sumte.review.dto.ReviewRequestDto;
import com.sumte.review.dto.ReviewResponseDto;
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
	public ReviewResponseDto createReview(Long userId, Long roomId, ReviewRequestDto dto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new SumteException(ReviewErrorCode.USER_NOT_FOUND));
		Room room = roomRepository.findById(dto.getRoomId())
			.orElseThrow(() -> new SumteException(ReviewErrorCode.ROOM_NOT_FOUND));

		Review review = ReviewConverter.toEntity(dto, user, room);
		Review saved = reviewRepository.save(review);
		return ReviewConverter.toDto(saved);
	}

	@Transactional
	public ReviewResponseDto updateReview(Long userId, Long id, ReviewRequestDto dto) {
		Review review = reviewRepository.findById(id)
			.orElseThrow(() -> new SumteException(ReviewErrorCode.REVIEW_NOT_FOUND));

		if (!review.getUser().getId().equals(userId)) {
			throw new SumteException(ReviewErrorCode.UNAUTHORIZED);
		}

		ReviewConverter.updateEntity(review, dto);
		return ReviewConverter.toDto(review);
	}

	@Transactional
	public void deleteReview(Long userId, Long id) {
		Review review = reviewRepository.findById(id)
			.orElseThrow(() -> new SumteException(ReviewErrorCode.REVIEW_NOT_FOUND));

		if (!review.getUser().getId().equals(userId)) {
			throw new SumteException(ReviewErrorCode.UNAUTHORIZED);
		}

		reviewRepository.delete(review);
	}

	@Transactional(readOnly = true)
	public Page<ReviewSearchDto> getReviewsByRoom(Long roomId, Pageable pageable) {
		return reviewRepository
			.findAllByRoomId(roomId, pageable)
			.map(this::toListDto);
	}

	private ReviewSearchDto toListDto(Review r) {
		return new ReviewSearchDto(
			r.getId(),
			r.getImageUrl(),
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
}
