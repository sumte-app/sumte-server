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

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetRoomResponse {
		String name;
		Long price;
		Long standardCount;
		Long totalCount;
		String content;
		LocalTime checkin;
		LocalTime checkout;
		String imageUrl;
	}

}
