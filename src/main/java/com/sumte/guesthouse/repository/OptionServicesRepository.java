package com.sumte.guesthouse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.guesthouse.entity.OptionServices;

public interface OptionServicesRepository extends JpaRepository<OptionServices, Long> {
	Optional<OptionServices> findByName(String name);

	List<OptionServices> findAll();
}
