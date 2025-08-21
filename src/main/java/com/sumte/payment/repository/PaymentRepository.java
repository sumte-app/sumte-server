package com.sumte.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.payment.entity.Payment;
import com.sumte.reservation.entity.Reservation;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
	Optional<Payment> findByReservation(Reservation reservation);

	List<Payment> findByReservationId(Long reservationId);

	void deleteByReservationId(Long reservationId);
}
