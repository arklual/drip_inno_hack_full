package com.dripteam.innoBackend.services;

import com.dripteam.innoBackend.entities.MemberEntity;
import com.dripteam.innoBackend.entities.ProjectEntity;
import com.dripteam.innoBackend.repositories.MemberRepository;
import com.dripteam.innoBackend.repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectService {
    private ProjectRepository repository;
    private MemberRepository members;

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


    public  List<MemberEntity> getMembersById(UUID id){
        return members.findByProjectId(id);
    }

    public  MemberEntity addMember(MemberEntity member){
        members.save(member);
        return member;
    }
    public List<ProjectEntity> findAll(){
        return repository.findAll();
    }
}
