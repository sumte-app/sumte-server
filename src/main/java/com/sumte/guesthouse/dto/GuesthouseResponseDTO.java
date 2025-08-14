package com.sumte.guesthouse.dto;

import java.util.List;

import com.sumte.guesthouse.entity.AdType;
import com.sumte.room.dto.RoomResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GuesthouseResponseDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Register {
		Long id;
		String name;
		String addressRegion;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Update {
		Long id;
		String name;
		String addressRegion;
		String addressDetail;
		List<String> optionServices;
		List<String> targetAudience;

	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class delete {
		String name;
		String addressDetail;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GetHouseResponse {
		Long id;
		String name;
		String addressRegion;
		String addressDetail;
		String information;
		String imageUrl;
		AdType advertisement;

		List<String> optionServices;
		List<String> targetAudience;
		List<RoomResponseDTO.GetRoomResponse> rooms;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HomeSummary {
		private Long guestHouseId;
		private String name;
		private String addressRegion;
		private String imageUrl;
		private Double averageScore;
		private int reviewCount;
		private String checkInTime;
		private Long minPrice;
		private boolean isAd;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class HomeCard {
		private Long id;
		private String name;
		private String addressRegion;
		private String addressDetail;
		private String imageUrl;
		private Long minPrice;
		private String checkin;
		private Long reviewCount;
		private Double averageScore;
	}

}
