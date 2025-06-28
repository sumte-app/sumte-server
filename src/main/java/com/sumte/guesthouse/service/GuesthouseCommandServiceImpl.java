package com.sumte.guesthouse.service;

import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.converter.GuesthouseConverter;
import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.repository.GuesthouseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GuesthouseCommandServiceImpl implements GuesthouseCommandService {

	private final GuesthouseRepository guesthouseRepository;
	private final GuesthouseConverter guesthouseConverter;

	@Override
	public GuesthouseResponseDTO.register registerGuesthouse(GuesthouseRequestDTO.register dto) {
		Guesthouse guesthouse = guesthouseConverter.toRegisterEntity(dto);

		// 중복 데이터인지 검사
		Guesthouse data = guesthouseRepository.findByNameAndAddressDetail(dto.getName(), dto.getAddressDetail());
		if (data != null) {
			throw new SumteException(CommonErrorCode.DUPLICATE_DATA);
		}

		guesthouseRepository.save(guesthouse);

		GuesthouseResponseDTO.register result = guesthouseConverter.toRegisterResponseDTO(guesthouse);
		return result;

	}

	@Override
	public GuesthouseResponseDTO.delete deleteGuesthouse(GuesthouseRequestDTO.delete dto) {
		Guesthouse guesthouse = guesthouseRepository.findByNameAndAddressDetail(dto.getName(), dto.getAddressDetail());
		if (guesthouse == null) {
			throw new SumteException(CommonErrorCode.NOT_EXIST);
		} else {
			guesthouseRepository.delete(guesthouse);
			return GuesthouseResponseDTO.delete.builder()
				.name(guesthouse.getName())
				.addressDetail(guesthouse.getAddressDetail())
				.build();
		}
	}

}
