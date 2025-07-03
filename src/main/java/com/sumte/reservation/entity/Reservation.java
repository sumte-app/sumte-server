package com.sumte.reservation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sumte.jpa.BaseTimeEntity;
import com.sumte.room.entity.Room;
import com.sumte.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Builder
@AllArgsConstructor
public class Reservation extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id")
	private Room room;

	private Long adultCount;
	private Long childCount;

	private LocalDate startDate;
	private LocalDate endDate;

	@Enumerated(EnumType.STRING)
	private ReservationStatus reservationStatus;

	public void cancel() {
		this.reservationStatus = ReservationStatus.CANCELED;
	}
}
