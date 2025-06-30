package com.sumte.reservation.repository;

import com.sumte.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

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

}
