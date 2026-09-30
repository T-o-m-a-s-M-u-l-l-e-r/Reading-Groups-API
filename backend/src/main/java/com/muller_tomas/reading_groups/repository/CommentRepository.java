package com.muller_tomas.reading_groups.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muller_tomas.reading_groups.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	Set<Comment> findAllByGroup_Id(int groupId);
	
}