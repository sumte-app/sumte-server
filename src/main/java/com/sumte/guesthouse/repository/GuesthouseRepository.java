package com.sumte.guesthouse.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sumte.guesthouse.entity.Guesthouse;

public interface GuesthouseRepository extends JpaRepository<Guesthouse, Long> {
	Guesthouse findByNameAndAddressDetail(String name, String addressDetails);

	Optional<Guesthouse> findById(Long id);

	//리뷰는 Page로 했는데 Slice로 수정해도 괜찮을거 같음 (확인 -> 전체 요소수는 필요없으니)
	@Query("SELECT g FROM Guesthouse g ORDER BY " + "CASE WHEN g.advertisement = 'AD' THEN 0 ELSE 1 END, g.name ASC")
	Slice<Guesthouse> findAllOrderedForHome(Pageable pageable);
}
