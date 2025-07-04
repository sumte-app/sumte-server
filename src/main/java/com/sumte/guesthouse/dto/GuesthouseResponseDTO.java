package com.sumte.guesthouse.dto;

import java.util.List;

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

		String name;
		String addressRegion;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Update {
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

}
