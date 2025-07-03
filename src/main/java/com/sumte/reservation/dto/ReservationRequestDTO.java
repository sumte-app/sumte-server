package com.sumte.reservation.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReservationRequestDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateReservationDTO {

		@NotNull
		private Long roomId;

		@NotNull
		@Min(value = 1, message = "성인 인원 수는 1명 이상이어야 합니다.")
		private Long adultCount;

		@NotNull
		@Min(value = 0, message = "아동 인원 수는 0명 이상이어야 합니다.")
		private Long childCount;

		@NotNull
		private LocalDate startDate;

		@NotNull
		private LocalDate endDate;
	}
}
