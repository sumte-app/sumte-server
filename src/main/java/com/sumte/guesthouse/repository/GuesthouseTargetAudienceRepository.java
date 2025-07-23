package com.sumte.guesthouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sumte.guesthouse.entity.mapping.GuesthouseTargetAudience;

public interface GuesthouseTargetAudienceRepository extends JpaRepository<GuesthouseTargetAudience, Long> {
	void deleteByGuesthouseId(Long guesthouseId);

	@Query("SELECT gta.targetAudience.name FROM GuesthouseTargetAudience gta WHERE gta.guesthouse.id = :guesthouseId")
	List<String> findTargetAudienceNamesByGuesthouseId(@Param("guesthouseId") Long guesthouseId);
}
