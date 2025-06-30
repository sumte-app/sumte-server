package com.sumte.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.room.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}