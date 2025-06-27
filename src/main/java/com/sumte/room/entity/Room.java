package com.sumte.room.entity;

import java.time.LocalTime;

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
public class Room extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guesthouse_id")
	private Guesthouse guesthouse;

	private String name;
	private String contents;
	private Long price;
	private LocalTime checkin;
	private LocalTime checkout;
	private Long standardCount;
	private Long totalCount;
	private String imageUrl;
}
