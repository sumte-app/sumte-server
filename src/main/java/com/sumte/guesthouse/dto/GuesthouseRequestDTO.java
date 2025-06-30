package com.sumte.guesthouse.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class GuesthouseRequestDTO {
	@Getter
	public static class Register {

		@NotBlank(message = "이름은 필수입니다.")
		String name;

		@NotBlank(message = "검색 시 노출될 최삳단 위치 키워드를 입력해주세요")
		String addressRegion;

		@NotBlank(message = "시 단위로 적어주세요 / 필수 값입니다.")
		String addressDetail;

		String information;
		String imageUrl;

		List<String> optionServices;
		List<String> targetAudience;

	}

	@Getter
	public static class Update {

	}

	@Getter
	public static class delete {
		@NotBlank(message = "이름은 필수입니다.")
		String name;

		@NotBlank(message = "시 단위 상세 주소를 적어주세요. / 필수 값 입니다.")
		String addressDetail;

	}
}
