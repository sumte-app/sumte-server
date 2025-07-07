package com.sumte.room.dto;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RoomResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Register {
		Long roomId;
		String name;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Update {
		Long roomId;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Delete {
		String name;
	}

	@NoArgsConstructor
	@Getter
	@AllArgsConstructor
	@Builder
	public static class RoomSummary {
		private Long id;
		private String name;
		private Long price;
		private String imageUrl;
		private Long standardCount;
		private Long totalCount;
		private LocalTime checkin;
		private LocalTime checkout;
	}

}
