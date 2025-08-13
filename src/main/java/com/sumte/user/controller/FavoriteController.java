package com.sumte.user.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sumte.security.authorization.UserId;
import com.sumte.user.dto.FavoriteResponseDto;
import com.sumte.user.service.FavoriteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "찜", description = "찜 관련 API")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
	private final FavoriteService favService;

	@Operation(summary = "찜 추가")
	@PostMapping("/{guesthouseId}")
	public ResponseEntity<Void> addFavorite(
		@PathVariable Long guesthouseId,
		@UserId Long userId
	) {
		favService.addFavorite(userId, guesthouseId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "찜 취소")
	@DeleteMapping("/{guesthouseId}")
	public ResponseEntity<Void> removeFavorite(
		@PathVariable Long guesthouseId,
		@UserId Long userId
	) {
		favService.removeFavorite(userId, guesthouseId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "사용자 찜 목록 조회")
	@GetMapping
	public ResponseEntity<Page<FavoriteResponseDto>> getFavorites(
		@UserId Long userId,
		@ParameterObject @PageableDefault(size = 10) Pageable pageable
	) {
		return ResponseEntity.ok(favService.getFavorites(userId, pageable));
	}

	@Operation(summary = "게스트하우스ID들 중 내가 찜한 게스트하우스만 조회")
	@GetMapping(params = "guesthouseIds")
	public ResponseEntity<List<Long>> getFavoritedIds(
		@UserId Long userId,
		@RequestParam List<Long> guesthouseIds
	) {
		return ResponseEntity.ok(favService.getFavoritedGuesthouseIds(userId, guesthouseIds));
	}

}