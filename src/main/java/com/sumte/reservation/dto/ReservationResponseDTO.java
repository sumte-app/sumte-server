package com.sumte.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
		private String status;
	}
}

