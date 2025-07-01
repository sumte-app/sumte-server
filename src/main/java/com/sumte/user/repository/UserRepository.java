package com.sumte.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumte.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}