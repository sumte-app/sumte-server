package com.sumte.room.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Optional<Room> findRoomByGuesthouseIdAndName(Long guesthouseId, String name);

    Optional<Room> findById(Long roomId);
}