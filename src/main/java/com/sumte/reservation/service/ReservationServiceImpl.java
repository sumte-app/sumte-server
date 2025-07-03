package com.sumte.reservation.service;

import com.sumte.apiPayload.code.error.CommonErrorCode;
import com.sumte.apiPayload.code.error.ReservationErrorCode;
import com.sumte.apiPayload.exception.SumteException;
import com.sumte.reservation.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sumte.reservation.converter.ReservationConverter;
import com.sumte.reservation.dto.ReservationRequestDTO;
import com.sumte.reservation.dto.ReservationResponseDTO;
import com.sumte.reservation.entity.Reservation;
import com.sumte.reservation.repository.ReservationRepository;
import com.sumte.room.entity.Room;
import com.sumte.room.repository.RoomRepository;
import com.sumte.user.entity.User;
import com.sumte.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

	private final ReservationRepository reservationRepository;
	private final ReservationConverter reservationConverter;
	private final RoomRepository roomRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public ReservationResponseDTO.CreateReservationDTO createReservation(ReservationRequestDTO.CreateReservationDTO request, Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new SumteException(CommonErrorCode.USER_NOT_FOUND));
		Room room = roomRepository.findById(request.getRoomId())
				.orElseThrow(() -> new SumteException(ReservationErrorCode.ROOM_NOT_FOUND));

		// 닐짜 유효성 검사
		if (request.getStartDate().isAfter(request.getEndDate()) || request.getStartDate().isEqual(request.getEndDate())) {
			throw new SumteException(ReservationErrorCode.RESERVATION_DATE_INVALID);
		}
		// 정원 초과 검사
		long totalPeople = request.getAdultCount() + request.getChildCount();
		if (room.getTotalCount() < totalPeople) {
			throw new SumteException(ReservationErrorCode.ROOM_CAPACITY_EXCEEDED);
		}
		// 중복 예약 검사
		boolean isOverlapping = reservationRepository.existsOverlappingReservation(room, request.getStartDate(), request.getEndDate());
		if(isOverlapping) {
			throw new SumteException(ReservationErrorCode.ALREADY_RESERVED);
		}

		Reservation reservation = reservationConverter.toEntity(request,user,room);
		reservationRepository.save(reservation);
		return reservationConverter.toCreateResponse(reservation);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ReservationResponseDTO.MyReservationDTO> getMyReservations(Long userId, Pageable pageable) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new SumteException(CommonErrorCode.USER_NOT_FOUND));

		Page<Reservation> reservations = reservationRepository.findAllByUser(user, pageable);
		return reservations.map(reservationConverter::toMyReservationDTO);
	}

	@Override
	@Transactional(readOnly = true)
	public ReservationResponseDTO.ReservationDetailDTO getReservationDetail(Long reservationId, Long userId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new SumteException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		if (!reservation.getUser().getId().equals(userId)) {
			throw new SumteException(CommonErrorCode.FORBIDDEN);
		}

		return reservationConverter.toReservationDetailDTO(reservation);
	}

	@Override
	@Transactional
	public void cancelReservation(Long reservationId, Long userId) {
		Reservation reservation = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new SumteException(ReservationErrorCode.RESERVATION_NOT_FOUND));

		// 사용자 본인 확인
		if (!reservation.getUser().getId().equals(userId)) {
			throw new SumteException(CommonErrorCode.FORBIDDEN);
		}
		// 이미 취소된 예약인지 확인
		if (reservation.getReservationStatus() == ReservationStatus.CANCELED) {
			throw new SumteException(ReservationErrorCode.ALREADY_CANCELED);
		}
		reservation.cancel();
	}

}
