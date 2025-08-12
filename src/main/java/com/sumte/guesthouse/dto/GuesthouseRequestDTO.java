package com.sumte.guesthouse.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class GuesthouseRequestDTO {
	@Getter
	@Schema(name = "GuesthouseRegisterRequest", description = "게스트하우스 등록 요청 DTO")
	public static class Register {

		@NotBlank(message = "이름은 필수입니다.")
		@Schema(description = "게스트하우스 이름", example = "한강뷰 게스트하우스")
		String name;

		@NotBlank(message = "검색 시 노출될 최상단 위치 키워드를 입력해주세요")
		@Schema(description = "주소(광역/도 단위)", example = "서귀포시")
		String addressRegion;

		@NotBlank(message = "시 단위로 적어주세요 / 필수 값입니다.")
		@Schema(description = "주소(시 단위)", example = "서귀포시 어쩌구길12")
		String addressDetail;

		@Schema(description = "게스트하우스 소개/정보", example = "시장 도보 10분, 해수욕장 도보 15분 거리")
		String information;

		@Schema(description = "대표 이미지 URL", example = "https://sumte-file.s3.ap-northeast-2.amazonaws.com/guesthouse_main.png")
		String imageUrl;

		@Schema(description = "부가 서비스 목록", example = "[\"조식포함\"]")
		List<String> optionServices;

		@Schema(description = "이용 대상 목록", example = "[\"애견동반\"]")
		List<String> targetAudience;
	}

	@Getter
	@Schema(name = "GuesthouseUpdateRequest", description = "게스트하우스 수정 요청 DTO")
	public static class Update {

		@Schema(description = "게스트하우스 이름", example = "제주 바다뷰 게스트하우스")
		String name;

		@Schema(description = "주소(광역/도 단위)", example = "제주특별자치도")
		String addressRegion;

		@Schema(description = "주소(시 단위)", example = "서귀포시 어쩌구 길 123")
		String addressDetail;

		@Schema(description = "게스트하우스 소개/정보", example = "서귀포 앞바다와 일출이 보이는 전통 한옥 스타일 숙소, 조식 제공")
		String information;

		@Schema(description = "대표 이미지 URL", example = "https://sumte-file.s3.ap-northeast-2.amazonaws.com/guesthouse_update_main.png")
		String imageUrl;

		@Schema(description = "옵션 서비스 목록", example = "[\"이벤트\"]")
		List<String> optionServices;

		@Schema(description = "대상 필터(배열)", example = "[\"애견동반\"]")
		List<String> targetAudience;
	}

	@Getter
	public static class delete {
		@NotBlank(message = "이름은 필수입니다.")
		String name;

		@NotBlank(message = "시 단위 상세 주소를 적어주세요. / 필수 값 입니다.")
		String addressDetail;

	}
}
