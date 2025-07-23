package com.sumte.room.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sumte.room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
	Optional<Room> findRoomByGuesthouseIdAndName(Long guesthouseId, String name);

	Optional<Room> findById(Long roomId);

	//홈화면
	@Query("SELECT MIN(r.price) FROM Room r WHERE r.guesthouse.id = :guesthouseId")
	Long findMinPriceByGuesthouseId(@Param("guesthouseId") Long guesthouseId);

	@Query("SELECT MIN(FUNCTION('TIME_FORMAT', r.checkin, '%H:%i')) "
		+ "FROM Room r WHERE r.guesthouse.id = :guesthouseId")
	String findEarliestCheckinByGuesthouseId(@Param("guesthouseId") Long guesthouseId);

	//객실 조회
	List<Room> findAllByGuesthouseId(Long guesthouseId);

}