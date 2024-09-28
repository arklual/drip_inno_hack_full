package com.dripteam.innoBackend.repositories;

import com.dripteam.innoBackend.entities.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, UUID> {
}
