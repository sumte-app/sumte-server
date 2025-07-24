package com.sumte.payment.repository;

import com.sumte.payment.entity.Payment;
import com.sumte.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByReservation(Reservation reservation);
}
