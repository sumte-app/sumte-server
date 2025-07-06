package com.sumte.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sumte.apiPayload.code.error.FavoriteErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.user.dto.FavoriteResponseDto;
import com.sumte.user.entity.Favorite;
import com.sumte.user.entity.User;
import com.sumte.user.repository.FavoriteRepository;
import com.sumte.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final UserRepository userRepository;
	private final GuesthouseRepository guesthouseRepository;

	@Transactional
	public void addFavorite(Long userId, Long guesthouseId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new SumteException(FavoriteErrorCode.USER_NOT_FOUND));
		Guesthouse gh = guesthouseRepository.findById(guesthouseId)
			.orElseThrow(() -> new SumteException(FavoriteErrorCode.GUESTHOUSE_NOT_FOUND));

		// 이미 있으면 그냥 리턴
		if (favoriteRepository.existsByUserIdAndGuesthouseId(userId, guesthouseId)) {
			return;
		}
		favoriteRepository.save(Favorite.create(user, gh));
	}

	@Transactional
	public void removeFavorite(Long userId, Long guesthouseId) {
		favoriteRepository
			.findByUserIdAndGuesthouseId(userId, guesthouseId)
			.ifPresent(favoriteRepository::delete);
	}

	@Transactional(readOnly = true)
	public Page<FavoriteResponseDto> getFavorites(Long userId, Pageable pageable) {
		return favoriteRepository
			.findAllByUserId(userId, pageable)             //페이징으로 엔티티 불러와 dto로 변환
			.map(fav -> new FavoriteResponseDto(
				fav.getGuesthouse().getId(),
				fav.getGuesthouse().getName()
			));
	}
}