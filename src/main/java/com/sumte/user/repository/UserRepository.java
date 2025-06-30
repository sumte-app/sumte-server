package com.sumte.user.repository;

// import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	// List<Review> findAllByRoom_Guesthouse_Id(Long guesthouseId);
}