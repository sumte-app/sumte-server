package com.sumte.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationResponseDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateReservationDTO {
		private Long reservationId;
	}
}
