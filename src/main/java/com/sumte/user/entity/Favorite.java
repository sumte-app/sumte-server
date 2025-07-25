package com.sumte.user.entity;

import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.jpa.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Favorite extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guesthouse_id")
	private Guesthouse guesthouse;

	public static Favorite create(User user, Guesthouse guesthouse) {
		Favorite fav = new Favorite();
		fav.user = user;
		fav.guesthouse = guesthouse;
		return fav;
	}

	//특정 유저의 특정 숙소 찜인지 판단할 때 사용, -> 삭제할 찜을 찾을때 사용하기 위함
	public boolean isOf(Long userId, Long guesthouseId) {
		return this.user.getId().equals(userId)
			&& this.guesthouse.getId().equals(guesthouseId);
	}
}

