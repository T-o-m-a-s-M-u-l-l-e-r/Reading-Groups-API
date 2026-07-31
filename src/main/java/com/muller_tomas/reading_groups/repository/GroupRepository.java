package com.muller_tomas.reading_groups.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muller_tomas.reading_groups.model.User;

public interface GroupRepository extends JpaRepository<User, Integer> {
	
}