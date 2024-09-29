package com.dripteam.innoBackend.repositories;

import com.dripteam.innoBackend.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, UUID> {
    List<MemberEntity> findByProjectId(UUID projectId);
}
