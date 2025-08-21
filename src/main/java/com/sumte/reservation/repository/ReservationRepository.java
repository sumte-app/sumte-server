package com.sumte.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sumte.reservation.entity.Reservation;
import com.sumte.reservation.entity.ReservationStatus;
import com.sumte.room.entity.Room;
import com.sumte.user.entity.User;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	@Query("""
		SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
		FROM Reservation r
		JOIN Payment p ON p.reservation = r
		WHERE r.room = :room
		  AND r.reservationStatus <> com.sumte.reservation.entity.ReservationStatus.CANCELED
		  AND p.paymentStatus = com.sumte.payment.entity.PaymentStatus.PAID
		  AND r.startDate < :endDate
		  AND r.endDate   > :startDate
		""")
	boolean existsOverlappingReservation(@Param("room") Room room,
		@Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate);

	List<Reservation> findByReservationStatusNot(ReservationStatus status);

	@Query("""
		select r
		from Reservation r
		where r.user = :user
		  and exists (
		    select 1
		    from Payment p
		    where p.reservation = r
		      and p.paymentStatus = com.sumte.payment.entity.PaymentStatus.PAID
		  )
		""")
	Page<Reservation> findAllPaidByUser(@Param("user") User user, Pageable pageable);

	@Query("""
		select r from Reservation r
		where r.room.guesthouse.id = :guesthouseId
		  and r.reservationStatus <> com.sumte.reservation.entity.ReservationStatus.CANCELED
		  and exists (
		      select 1
		      from Payment p
		      where p.reservation = r
		        and p.paymentStatus = com.sumte.payment.entity.PaymentStatus.PAID
		  )
		  and r.startDate < :endExclusive
		  and r.endDate > :startInclusive
		""")
	List<Reservation> findActivePaidByGuesthouseAndOverlap(
		@Param("guesthouseId") Long guesthouseId,
		@Param("startInclusive") java.time.LocalDate startInclusive,
		@Param("endExclusive") java.time.LocalDate endExclusive
	);

	@Query("""
		select r from Reservation r
		where r.room = :room
		  and r.reservationStatus <> com.sumte.reservation.entity.ReservationStatus.CANCELED
		  and exists (
		      select 1
		      from Payment p
		      where p.reservation = r
		        and p.paymentStatus = com.sumte.payment.entity.PaymentStatus.PAID
		  )
		  and r.startDate < :endExclusive
		  and r.endDate > :startInclusive
		""")
	List<Reservation> findActivePaidByRoomAndOverlap(
		@Param("room") com.sumte.room.entity.Room room,
		@Param("startInclusive") java.time.LocalDate startInclusive,
		@Param("endExclusive") java.time.LocalDate endExclusive
	);

	List<Reservation> findByRoomId(Long roomId);

	void deleteByRoomId(Long roomId);

}
