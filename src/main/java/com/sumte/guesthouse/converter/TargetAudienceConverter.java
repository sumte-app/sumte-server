package com.sumte.guesthouse.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sumte.guesthouse.dto.OptionResponseDTO;
import com.sumte.guesthouse.entity.TargetAudience;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TargetAudienceConverter {
	public static OptionResponseDTO.Target toTarget(TargetAudience targetAudience) {
		return OptionResponseDTO.Target.builder()
			.name(targetAudience.getName())
			.build();
	}

	public static OptionResponseDTO.TargetList toTargetList(List<TargetAudience> targetAudiences) {

		List<OptionResponseDTO.Target> optionList = targetAudiences.stream()
			.map(TargetAudienceConverter::toTarget).collect(Collectors.toList());

		return OptionResponseDTO.TargetList.builder()
			.targets(optionList)
			.build();

	}
}
