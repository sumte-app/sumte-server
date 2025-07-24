package com.sumte.reservation.repository;

import com.sumte.reservation.entity.ReservationStatus;
import com.sumte.room.entity.Room;
import com.sumte.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Query("""
	SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
	FROM Reservation r
	WHERE r.room = :room
	  AND r.startDate < :endDate
	  AND r.endDate > :startDate""")
    boolean existsOverlappingReservation(@Param("room") Room room,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

	Page<Reservation> findAllByUser(User user, Pageable pageable);

	List<Reservation> findByReservationStatusNot(ReservationStatus status);

}
