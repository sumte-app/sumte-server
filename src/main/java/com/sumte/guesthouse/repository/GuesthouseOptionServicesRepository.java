package com.sumte.guesthouse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sumte.guesthouse.entity.mapping.GuesthouseOptionServices;

public interface GuesthouseOptionServicesRepository extends JpaRepository<GuesthouseOptionServices, Long> {
	Boolean existsByGuesthouseIdAndOptionServicesId(Long guesthouseId, Long optionServicesId);

	void deleteByGuesthouseId(Long guesthouseId);

	@Query("SELECT gos.optionServices.name FROM GuesthouseOptionServices gos WHERE gos.guesthouse.id = :guesthouseId")
	List<String> findTargetAudienceNamesByGuesthouseId(@Param("guesthouseId") Long guesthouseId);
}
