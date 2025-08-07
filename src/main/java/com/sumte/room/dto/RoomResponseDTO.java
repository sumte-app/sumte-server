package com.sumte.room.dto;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RoomResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Registered {
		Long roomId;
		String name;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Updated {
		Long roomId;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Deleted {
		String name;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetRoomResponse {
		Long id;
		String name;
		Long price;
		Long standardCount;
		Long totalCount;
		String content;
		LocalTime checkin;
		LocalTime checkout;
		List<String> imageUrls;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetPreviewRoomByGuesthouseResponse {
		Long id;
		String name;
		Long price;
		Long standardCount;
		Long totalCount;
		String content;
		LocalTime checkin;
		LocalTime checkout;
		String imageUrl;
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
		private boolean isReservable;
	}

}
