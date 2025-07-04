package com.sumte.review.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	// 게스트하우스 내 전체 리뷰 조회
	Page<Review> findAllByRoomGuesthouseId(Long guesthouseId, Pageable pageable);

	//피드백 반영 (로직 개선)
	Optional<Review> findByIdAndUserId(Long reviewId, Long userId);

	//내가 작성한 리뷰 조회 (단순 내가 작성한 리뷰만 조회하는거라 넣었는데 다시 확인(중복) )
	Page<Review> findAllByUserId(Long userId, Pageable pageable);
}