package com.sumte.guesthouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.guesthouse.entity.mapping.GuesthouseTargetAudience;

public interface GuesthouseTargetAudienceRepository extends JpaRepository<GuesthouseTargetAudience, Long> {
}
