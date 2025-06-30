package com.sumte.reservation.service;

import com.sumte.reservation.dto.ReservationRequestDTO;
import com.sumte.reservation.dto.ReservationResponseDTO;

public interface ReservationService {
	ReservationResponseDTO.CreateReservationDTO createReservation(ReservationRequestDTO.CreateReservationDTO request,Long UserId);
}
