package com.sumte.guesthouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.guesthouse.entity.mapping.GuesthouseOptionServices;

public interface GuesthouseOptionServicesRepository extends JpaRepository<GuesthouseOptionServices, Long> {
	Boolean existsByGuesthouseIdAndOptionServicesId(Long guesthouseId, Long optionServicesId);

	void deleteByGuesthouseId(Long guesthouseId);
}
