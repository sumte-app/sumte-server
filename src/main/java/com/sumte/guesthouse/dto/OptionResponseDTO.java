package com.sumte.guesthouse.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OptionResponseDTO {

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Target {
		String name;
	}

	// 페이징 처리 안한 이유는 어차피 targetAudience 나 OptionService같은 경우엔
	// 굳이 필요 없을 것 같기 때문입니다. 전체 조회 api 이기도 하구요
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class TargetList {
		List<Target> targets;
	}

	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Option {
		String name;
	}

	// 페이징 처리 안한 이유는 어차피 targetAudience 나 OptionService같은 경우엔
	// 굳이 필요 없을 것 같기 때문입니다. 전체 조회 api 이기도 하구요
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OptionList {
		List<Option> options;
	}

}
