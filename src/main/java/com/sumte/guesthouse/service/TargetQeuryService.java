package com.sumte.guesthouse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sumte.guesthouse.converter.TargetAudienceConverter;
import com.sumte.guesthouse.dto.OptionResponseDTO;
import com.sumte.guesthouse.entity.TargetAudience;
import com.sumte.guesthouse.repository.TargetAudienceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TargetQeuryService {

	private final TargetAudienceRepository targetAudienceRepository;
	private final TargetAudienceConverter targetAudienceConverter;

	public OptionResponseDTO.TargetList getAllTargetAudience() {
		List<TargetAudience> targetAudienceList = targetAudienceRepository.findAll();

		OptionResponseDTO.TargetList targetList = targetAudienceConverter.toTargetList(targetAudienceList);

		return targetList;
	}
}
