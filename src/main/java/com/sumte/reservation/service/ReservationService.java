package com.sumte.reservation.service;

import com.sumte.reservation.dto.ReservationRequestDTO;
import com.sumte.reservation.dto.ReservationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservationService {
	ReservationResponseDTO.CreateReservationDTO createReservation(ReservationRequestDTO.CreateReservationDTO request,Long UserId);
	Page<ReservationResponseDTO.MyReservationDTO> getMyReservations(Long userId, Pageable pageable);
	ReservationResponseDTO.ReservationDetailDTO getReservationDetail(Long reservationId, Long userId);
	void cancelReservation(Long reservationId, Long userId);
	void updateCompletedReservations();
}
