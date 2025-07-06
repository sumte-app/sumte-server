package com.sumte.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.user.entity.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
	// 이미 찜했는지 확인
	Optional<Favorite> findByUserIdAndGuesthouseId(Long userId, Long guesthouseId);

	//찜 여부 확인하기 위함
	boolean existsByUserIdAndGuesthouseId(Long userId, Long guesthouseId);

	// 사용자가 찜한 목록 조회
	Page<Favorite> findAllByUserId(Long userId, Pageable pageable);
}