package com.sumte.review.entity;

import com.sumte.jpa.BaseTimeEntity;
import com.sumte.room.entity.Room;
import com.sumte.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	private String imageUrl;
	private String contents;
	private int score;

	//도메인 메서드 추가
	// 리뷰 작성 시 연관 관계 설정 -> 리뷰생성할때 방과 사용자가 묶여서 생성
	public void assignUserAndRoom(User user, Room room) {
		this.user = user;
		this.room = room;
	}

	// 이미지 URL 변경
	public void changeImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	// 내용 변경
	public void changeContents(String contents) {
		this.contents = contents;
	}

	// 평점 변경
	public void changeScore(int score) {
		this.score = score;
	}
}