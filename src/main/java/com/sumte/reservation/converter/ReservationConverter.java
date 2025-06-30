package com.sumte.reservation.converter;

import org.springframework.stereotype.Component;

import com.sumte.reservation.dto.ReservationRequestDTO;
import com.sumte.reservation.dto.ReservationResponseDTO;
import com.sumte.reservation.entity.Reservation;
import com.sumte.reservation.entity.ReservationStatus;
import com.sumte.room.entity.Room;
import com.sumte.user.entity.User;

@Component
public class ReservationConverter {

	public Reservation toEntity(ReservationRequestDTO.CreateReservationDTO request, User user, Room room) {
		return Reservation.builder()
			.user(user)
			.room(room)
			.adultCount(request.getAdultCount())
			.childCount(request.getChildCount())
			.startDate(request.getStartDate())
			.endDate(request.getEndDate())
			.reservationStatus(ReservationStatus.RESERVED)
			.build();
	}

	public ReservationResponseDTO.CreateReservationDTO toCreateResponse(Reservation reservation) {
		return ReservationResponseDTO.CreateReservationDTO.builder()
			.reservationId(reservation.getId())
			.build();
	}
}
