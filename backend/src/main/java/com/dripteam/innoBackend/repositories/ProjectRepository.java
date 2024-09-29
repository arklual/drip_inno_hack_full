package com.dripteam.innoBackend.repositories;


import com.dripteam.innoBackend.entities.MemberEntity;
import com.dripteam.innoBackend.entities.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {
}
