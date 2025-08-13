package com.sumte.review.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumte.apiPayload.code.error.ReviewErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.image.entity.Image;
import com.sumte.image.entity.OwnerType;
import com.sumte.image.repository.ImageRepository;
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
	private final ImageRepository imageRepository;

	@Transactional
	public Long createReview(Long userId, ReviewRequestDto dto) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new SumteException(ReviewErrorCode.USER_NOT_FOUND));
		Room room = roomRepository.findById(dto.getRoomId())
			.orElseThrow(() -> new SumteException(ReviewErrorCode.ROOM_NOT_FOUND));

		Review review = ReviewConverter.toEntity(dto, user, room);
		Review saved = reviewRepository.save(review);
		List<String> urls = dto.getImageUrls();

		if (urls != null && !urls.isEmpty()) {
			var imagesToSave = buildReviewImages(saved.getId(), urls);
			if (!imagesToSave.isEmpty()) {
				imageRepository.saveAll(imagesToSave);
			}
		}

		return saved.getId();
	}

	@Transactional
	public Void updateReview(Long userId, Long reviewId, ReviewRequestDto dto) {
		Optional<Review> opt = reviewRepository.findByIdAndUserId(reviewId, userId);
		if (opt.isPresent()) {
			Review review = opt.get();
			ReviewConverter.updateEntity(review, dto);

			// 이미지 변경 규칙
			// null  -> 이미지 변경 안 함
			// []    -> 기존 이미지 모두 삭제
			// [..]  -> 기존 삭제 후 새로 저장
			if (dto.getImageUrls() != null) {
				imageRepository.deleteByOwnerTypeAndOwnerId(OwnerType.REVIEW, reviewId);

				List<String> urls = dto.getImageUrls();
				if (!urls.isEmpty()) {
					var imagesToSave = buildReviewImages(reviewId, urls);
					if (!imagesToSave.isEmpty()) {
						imageRepository.saveAll(imagesToSave);
					}
				}
			}
			return null;
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

	@Transactional(readOnly = true)
	public Page<ReviewSearchDto> getReviewsByGuesthouse(Long guesthouseId, Pageable pageable) {
		// 1) 페이징된 리뷰 조회
		Page<Review> reviewPage = reviewRepository
			.findAllByRoomGuesthouseId(guesthouseId, pageable);

		List<Review> reviews = reviewPage.getContent();
		if (reviews.isEmpty()) {
			return Page.empty(pageable);
		}

		// 2) 리뷰 ID 리스트 뽑아서 이미지 일괄 조회
		List<Long> reviewIds = reviews.stream()
			.map(Review::getId)
			.toList();

		List<Image> images = imageRepository
			.findByOwnerTypeAndOwnerIdInOrderByOwnerIdAscSortOrderAsc(
				OwnerType.REVIEW, reviewIds
			);

		// 3) ownerId 별로 URL 그룹핑
		Map<Long, List<String>> urlsByReview = images.stream()
			.collect(Collectors.groupingBy(
				Image::getOwnerId,
				Collectors.mapping(Image::getUrl, Collectors.toList())
			));

		// 4) DTO 변환 (이미 없는 toListDto 대신 직접 매핑)
		List<ReviewSearchDto> dtos = reviews.stream()
			.map(r -> new ReviewSearchDto(
				r.getId(),
				urlsByReview.getOrDefault(r.getId(), Collections.emptyList()),
				r.getContents(),
				r.getScore(),
				r.getUser().getNickname(),
				r.getCreatedAt()
			))
			.toList();

		// 5) PageImpl 으로 포장해서 반환
		return new PageImpl<>(
			dtos,
			pageable,
			reviewPage.getTotalElements()
		);
	}

	//리뷰테이블에서 userid와 일치하는 모든 리뷰 가져오기, 이 각 엔티티->dto로 변환
	@Transactional(readOnly = true)
	public Page<ReviewSearchDto> getMyReviews(Long userId, Pageable pageable) {
		// 1) 내가 쓴 리뷰 페이징 조회
		Page<Review> reviewPage = reviewRepository
			.findAllByUserId(userId, pageable);

		List<Review> reviews = reviewPage.getContent();
		if (reviews.isEmpty()) {
			return Page.empty(pageable);
		}

		// 2) 리뷰 ID 리스트
		List<Long> reviewIds = reviews.stream()
			.map(Review::getId)
			.toList();

		// 3) 해당 리뷰들의 이미지 일괄 조회
		List<Image> images = imageRepository
			.findByOwnerTypeAndOwnerIdInOrderByOwnerIdAscSortOrderAsc(
				OwnerType.REVIEW,
				reviewIds
			);

		// 4) ownerId 별 URL 그룹핑
		Map<Long, List<String>> urlsByReview = images.stream()
			.collect(Collectors.groupingBy(
				Image::getOwnerId,
				Collectors.mapping(Image::getUrl, Collectors.toList())
			));

		// 5) DTO 변환
		List<ReviewSearchDto> dtos = reviews.stream()
			.map(r -> new ReviewSearchDto(
				r.getId(),
				urlsByReview.getOrDefault(r.getId(), Collections.emptyList()),
				r.getContents(),
				r.getScore(),
				r.getUser().getNickname(),
				r.getCreatedAt()
			))
			.toList();

		// 6) PageImpl 포장
		return new PageImpl<>(
			dtos,
			pageable,
			reviewPage.getTotalElements()
		);
	}

	private List<Image> buildReviewImages(Long reviewId, List<String> rawUrls) {
		int[] idx = {0};
		return rawUrls.stream()
			.filter(u -> u != null && !u.isBlank())
			.distinct()  // 선택: 중복 제거
			.limit(10)   // 선택: 최대 10장
			.map(u -> Image.builder()
				.ownerType(OwnerType.REVIEW)
				.ownerId(reviewId)
				.url(u.trim())
				.sortOrder(idx[0]++)
				.build())
			.toList();
	}
}
