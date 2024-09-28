package com.dripteam.innoBackend.services;

import com.dripteam.innoBackend.entities.ProjectEntity;
import com.dripteam.innoBackend.repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectService {
    private ProjectRepository repository;

    public ProjectEntity addProject(ProjectEntity project){
        repository.save(project);
        return project;
    }

    public void deleteProject(ProjectEntity project){
        repository.delete(project);
    }

    public Optional<ProjectEntity> findProjectById(UUID id){
        return repository.findById(id);
    }
}
