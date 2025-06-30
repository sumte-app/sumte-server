package com.sumte.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	//게스트하우스 내 각 방별 리뷰 (피그마에 리뷰관련 내용이 아직 부실한 것 같아 일단 추가)
	Page<Review> findAllByRoomId(Long roomId, Pageable pageable);

	// 게스트하우스 내 전체 리뷰 조회
	Page<Review> findAllByRoomGuesthouseId(Long guesthouseId, Pageable pageable);
}