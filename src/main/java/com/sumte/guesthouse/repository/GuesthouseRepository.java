package com.sumte.guesthouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.guesthouse.entity.Guesthouse;

public interface GuesthouseRepository extends JpaRepository<Guesthouse, Long> {
}