package com.muller_tomas.reading_groups.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muller_tomas.reading_groups.model.Group;

public interface GroupRepository extends JpaRepository<Group, Integer> {
	
	Set<Group> findByUsersId(int userId);
	
}