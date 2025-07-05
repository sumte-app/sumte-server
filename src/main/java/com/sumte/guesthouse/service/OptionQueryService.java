package com.sumte.guesthouse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sumte.guesthouse.converter.OptionServiceConverter;
import com.sumte.guesthouse.dto.OptionResponseDTO;
import com.sumte.guesthouse.entity.OptionServices;
import com.sumte.guesthouse.repository.OptionServicesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptionQueryService {

	private final OptionServicesRepository optionServicesRepository;
	private final OptionServiceConverter optionServiceConverter;

	public OptionResponseDTO.OptionList getAllOptionServices() {
		List<OptionServices> optionServicesList = optionServicesRepository.findAll();

		OptionResponseDTO.OptionList optionList = optionServiceConverter.toOptionList(optionServicesList);

		return optionList;
	}

}
