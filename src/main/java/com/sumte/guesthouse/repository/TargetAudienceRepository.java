package com.sumte.guesthouse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.guesthouse.entity.TargetAudience;

public interface TargetAudienceRepository extends JpaRepository<TargetAudience, Long> {
	Optional<TargetAudience> findByName(String name);
}
