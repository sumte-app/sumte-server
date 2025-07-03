package com.sumte.guesthouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.guesthouse.entity.Guesthouse;

public interface GuesthouseRepository extends JpaRepository<Guesthouse, Long> {
	Guesthouse findByNameAndAddressDetail(String name, String addressDetails);

	Optional<Guesthouse> findById(Long id);
}
