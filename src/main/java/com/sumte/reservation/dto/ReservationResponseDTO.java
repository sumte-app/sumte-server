package com.sumte.reservation.dto;

import com.sumte.reservation.entity.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationResponseDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateReservationDTO {
		private Long reservationId;
	}

	@Getter
	@Builder
	public static class MyReservationDTO {
		private Long id;
		private String guestHouseName;
		private String roomName;
		private String imageUrl;
		private LocalDate startDate;
		private LocalDate endDate;
		private Long adultCount;
		private Long childCount;
		private int nightCount;
		private ReservationStatus status;

		private Long roomId;
		private boolean canWriteReview;
		private boolean reviewWritten;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReservationDetailDTO {
		private Long reservationId;
		private String guestHouseName;
		private String roomName;
		private String imageUrl;
		private Long adultCount;
		private Long childCount;
		private LocalDate startDate;
		private LocalDate endDate;
		private String status;
		private int nightCount;
		private Long totalPrice;
		private LocalDateTime reservedAt;
	}
}

