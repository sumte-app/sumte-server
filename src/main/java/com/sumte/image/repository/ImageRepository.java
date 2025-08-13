package com.sumte.image.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sumte.image.entity.Image;
import com.sumte.image.entity.OwnerType;

public interface ImageRepository extends JpaRepository<Image, Long> {

	boolean existsByOwnerTypeAndOwnerId(OwnerType ownerType, Long ownerId);

	@Query("SELECT MAX(i.sortOrder) FROM Image i WHERE i.ownerType = :ownerType AND i.ownerId = :ownerId")
	Optional<Integer> findMaxSortOrder(
		@Param("ownerType") OwnerType ownerType,
		@Param("ownerId") Long ownerId
	);

	List<Image> findByOwnerTypeAndOwnerIdOrderBySortOrderAsc(OwnerType ownerType, Long ownerId);

	List<Image> findByOwnerTypeAndOwnerIdInOrderByOwnerIdAscSortOrderAsc(OwnerType ownerType, List<Long> ownerIds);

	void deleteByOwnerTypeAndOwnerId(OwnerType ownerType, Long ownerId);
}
