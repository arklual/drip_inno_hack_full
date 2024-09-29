package com.dripteam.innoBackend.repositories;


import com.dripteam.innoBackend.entities.DeskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeskRepository extends JpaRepository<DeskEntity, UUID> {
    List<DeskEntity> findByProjectId(UUID projectId);
}
