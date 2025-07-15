package com.sumte.guesthouse.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sumte.guesthouse.dto.OptionResponseDTO;
import com.sumte.guesthouse.entity.OptionServices;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OptionServiceConverter {
	public static OptionResponseDTO.Option toOption(OptionServices optionServices) {
		return OptionResponseDTO.Option.builder()
			.name(optionServices.getName())
			.build();
	}

	public static OptionResponseDTO.OptionList toOptionList(List<OptionServices> optionServices) {

		List<OptionResponseDTO.Option> optionList = optionServices.stream()
			.map(OptionServiceConverter::toOption).collect(Collectors.toList());

		return OptionResponseDTO.OptionList.builder()
			.options(optionList)
			.build();

	}
}
