package com.muller_tomas.reading_groups.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.muller_tomas.reading_groups.model.Invite;

public interface InviteRepository extends JpaRepository<Invite, Integer> {

    Set<Invite> findAllByRecipient_Id(int userId);

}