package com.muller_tomas.reading_groups.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muller_tomas.reading_groups.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);
	
}
