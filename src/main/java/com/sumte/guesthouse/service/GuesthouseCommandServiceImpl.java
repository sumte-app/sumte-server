package com.sumte.guesthouse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.guesthouse.converter.GuesthouseConverter;
import com.sumte.guesthouse.dto.GuesthouseRequestDTO;
import com.sumte.guesthouse.dto.GuesthouseResponseDTO;
import com.sumte.guesthouse.entity.Guesthouse;
import com.sumte.guesthouse.entity.OptionServices;
import com.sumte.guesthouse.entity.TargetAudience;
import com.sumte.guesthouse.entity.mapping.GuesthouseOptionServices;
import com.sumte.guesthouse.entity.mapping.GuesthouseTargetAudience;
import com.sumte.guesthouse.repository.GuesthouseOptionServicesRepository;
import com.sumte.guesthouse.repository.GuesthouseRepository;
import com.sumte.guesthouse.repository.GuesthouseTargetAudienceRepository;
import com.sumte.guesthouse.repository.OptionServicesRepository;
import com.sumte.guesthouse.repository.TargetAudienceRepository;
import com.sumte.payment.entity.Payment;
import com.sumte.payment.repository.PaymentRepository;
import com.sumte.payment.repository.PaymentTermsRepository;
import com.sumte.payment.repository.RefundRepository;
import com.sumte.reservation.entity.Reservation;
import com.sumte.reservation.repository.ReservationRepository;
import com.sumte.review.repository.ReviewRepository;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;
import com.sumte.user.repository.FavoriteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GuesthouseCommandServiceImpl implements GuesthouseCommandService {

	private final GuesthouseRepository guesthouseRepository;
	private final GuesthouseConverter guesthouseConverter;
	private final OptionServicesRepository optionServicesRepository;
	private final TargetAudienceRepository targetAudienceRepository;
	private final GuesthouseTargetAudienceRepository guesthouseTargetAudienceRepository;
	private final GuesthouseOptionServicesRepository guesthouseOptionServicesRepository;
	private final FavoriteRepository favoriteRepository;
	private final ReviewRepository reviewRepository;
	private final ReservationRepository reservationRepository;
	private final RoomRepository roomRepository;
	private final PaymentRepository paymentRepository;
	private final RefundRepository refundRepository;
	private final PaymentTermsRepository paymentTermsRepository;

	@Override
	public GuesthouseResponseDTO.Register registerGuesthouse(GuesthouseRequestDTO.Register dto) {
		Guesthouse guesthouse = guesthouseConverter.toRegisterEntity(dto);

		// 중복 데이터인지 검사
		Guesthouse data = guesthouseRepository.findByNameAndAddressDetail(dto.getName(), dto.getAddressDetail());
		if (data != null) {
			throw new SumteException(CommonErrorCode.DUPLICATE_DATA);
		}

		guesthouseRepository.save(guesthouse);

		if (dto.getOptionServices() != null) {
			dto.getOptionServices().forEach(optionService -> {
				OptionServices optionServices = optionServicesRepository.findByName(optionService).orElseThrow(
					() -> new SumteException(CommonErrorCode.OPTIONSERVICE_NOT_EXIST)
				);

				GuesthouseOptionServices guesthouseOptionServices = new GuesthouseOptionServices();
				guesthouseOptionServices.setGuesthouse(guesthouse);
				guesthouseOptionServices.setOptionServices(optionServices);
				guesthouseOptionServicesRepository.save(guesthouseOptionServices);
			});
		}

		if (dto.getTargetAudience() != null) {
			dto.getTargetAudience().forEach(targetAudience -> {
				TargetAudience ta = targetAudienceRepository.findByName(targetAudience)
					.orElseThrow(() -> new SumteException(CommonErrorCode.TARGETAUDIENCE_NOT_EXIST));

				GuesthouseTargetAudience guesthouseTargetAudience = new GuesthouseTargetAudience();
				guesthouseTargetAudience.setGuesthouse(guesthouse);
				guesthouseTargetAudience.setTargetAudience(ta);

				guesthouseTargetAudienceRepository.save(guesthouseTargetAudience);
			});
		}

		GuesthouseResponseDTO.Register result = guesthouseConverter.toRegisterResponseDTO(guesthouse);
		return result;

	}

	@Override
	@Transactional
	public GuesthouseResponseDTO.Update updateGuesthouse(Long guesthouseId, GuesthouseRequestDTO.Update dto) {
		Guesthouse guesthouse = guesthouseRepository.findById(guesthouseId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST));
		if (dto.getName() != null) {
			guesthouse.setName(dto.getName());
		}
		if (dto.getAddressRegion() != null) {
			guesthouse.setAddressRegion(dto.getAddressRegion());
		}
		if (dto.getAddressDetail() != null) {
			guesthouse.setAddressDetail(dto.getAddressDetail());
		}
		if (dto.getInformation() != null) {
			guesthouse.setInformation(dto.getInformation());
		}
		if (dto.getOptionServices() != null) {
			List<String> option = dto.getOptionServices();

			// 일단 뭐가 됐든 현재 있던 속성 삭제하기
			guesthouseOptionServicesRepository.deleteByGuesthouseId(guesthouseId);

			option.forEach(optionService -> {
				OptionServices optionServices = optionServicesRepository.findByName(optionService).orElseThrow(
					() -> new SumteException(CommonErrorCode.OPTIONSERVICE_NOT_EXIST)
				);
				GuesthouseOptionServices guesthouseOptionServices = new GuesthouseOptionServices();
				guesthouseOptionServices.setGuesthouse(guesthouse);
				guesthouseOptionServices.setOptionServices(optionServices);
				guesthouseOptionServicesRepository.save(guesthouseOptionServices);
			});
		}

		if (dto.getTargetAudience() != null) {
			List<String> audience = dto.getTargetAudience();

			guesthouseTargetAudienceRepository.deleteByGuesthouseId(guesthouseId);

			audience.forEach(audienceService -> {
				TargetAudience targetAudience = targetAudienceRepository.findByName(audienceService)
					.orElseThrow(() -> new SumteException(CommonErrorCode.TARGETAUDIENCE_NOT_EXIST));

				GuesthouseTargetAudience guesthouseTargetAudience = new GuesthouseTargetAudience();
				guesthouseTargetAudience.setGuesthouse(guesthouse);
				guesthouseTargetAudience.setTargetAudience(targetAudience);
				guesthouseTargetAudienceRepository.save(guesthouseTargetAudience);

			});
		}

		return guesthouseConverter.toUpdateResponseDTO(guesthouse, dto.getOptionServices(), dto.getTargetAudience());

	}

	@Override
	@Transactional
	public GuesthouseResponseDTO.delete deleteGuesthouse(Long guesthouseId) {
		Guesthouse guesthouse = guesthouseRepository.findById(guesthouseId).orElse(null);

		if (guesthouse == null) {
			throw new SumteException(CommonErrorCode.NOT_EXIST);
		}

		// 1. guesthouse 하위 room 리스트 조회
		List<Room> rooms = roomRepository.findByGuesthouseId(guesthouseId);

		for (Room room : rooms) {
			Long roomId = room.getId();

			// 2. 해당 room의 reservation 조회
			List<Reservation> reservations = reservationRepository.findByRoomId(roomId);
			for (Reservation reservation : reservations) {
				Long reservationId = reservation.getId();

				// 3. reservation의 payment 조회
				List<Payment> payments = paymentRepository.findByReservationId(reservationId);
				for (Payment payment : payments) {
					Long paymentId = payment.getId();

					// 4. paymentTerms, refund 먼저 삭제
					paymentTermsRepository.deleteByPaymentId(paymentId);
					refundRepository.deleteByPaymentId(paymentId);
				}

				// 5. payment 삭제
				paymentRepository.deleteByReservationId(reservationId);

				// 7. 리뷰 삭제
				reviewRepository.deleteByReservationId(reservationId);

			}
			// 6. reservation 삭제
			reservationRepository.deleteByRoomId(roomId);
			reviewRepository.deleteByRoomId(roomId);

		}

		// 9. Guesthouse 관련 연관 테이블 삭제
		guesthouseOptionServicesRepository.deleteByGuesthouseId(guesthouseId);
		guesthouseTargetAudienceRepository.deleteByGuesthouseId(guesthouseId);
		favoriteRepository.deleteByGuesthouseId(guesthouseId);

		// 11. Guesthouse 삭제 (room은 cascade로 같이 삭제됨)
		guesthouseRepository.delete(guesthouse);
		return GuesthouseResponseDTO.delete.builder()
			.name(guesthouse.getName())
			.addressDetail(guesthouse.getAddressDetail())
			.build();
	}

	@Override
	@Transactional
	public void activateAdvertisement(Long guesthouseId) {
		Guesthouse guesthouse = guesthouseRepository.findById(guesthouseId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST));
		guesthouse.activateAd();
	}

	@Override
	@Transactional
	public void deactivateAdvertisement(Long guesthouseId) {
		Guesthouse guesthouse = guesthouseRepository.findById(guesthouseId)
			.orElseThrow(() -> new SumteException(CommonErrorCode.NOT_EXIST));
		guesthouse.deactivateAd();
	}

}
