package com.sumte.review.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.review.dto.ReviewRequestDto;
import com.sumte.review.dto.ReviewSearchDto;
import com.sumte.review.service.ReviewService;
import com.sumte.security.authorization.UserId;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "리뷰", description = "리뷰 관련 API")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	@Operation(summary = "리뷰 등록")
	@PostMapping
	public ResponseEntity<Void> createReview(
		@UserId Long userId,
		@RequestBody @Valid ReviewRequestDto dto) {
		reviewService.createReview(userId, dto.getRoomId(), dto);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "리뷰 수정")
	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateReview(
		@UserId Long userId,
		@PathVariable Long id,
		@RequestBody @Valid ReviewRequestDto dto) {
		reviewService.updateReview(userId, id, dto);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "리뷰 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteReview(
		@UserId Long userId,
		@PathVariable Long id) {
		reviewService.deleteReview(userId, id);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "게스트하우스 전체 리뷰 조회")
	@GetMapping("/guesthouse/{guesthouseId}")
	public ResponseEntity<Page<ReviewSearchDto>> getReviewsByGuesthouse(
		@PathVariable Long guesthouseId,
		@ParameterObject
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		Page<ReviewSearchDto> page = reviewService.getReviewsByGuesthouse(guesthouseId, pageable);
		return ResponseEntity.ok(page);
	}

	@Operation(summary = "내가 남긴 리뷰 전체 조회")
	@GetMapping("/myreviews")
	public ResponseEntity<Page<ReviewSearchDto>> getMyReviews(
		@UserId Long userId,
		@ParameterObject
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
		Pageable pageable
	) {
		Page<ReviewSearchDto> page = reviewService.getMyReviews(userId, pageable);
		return ResponseEntity.ok(page);
	}
}

