package com.dripteam.innoBackend.services;

import com.dripteam.innoBackend.entities.DeskEntity;
import com.dripteam.innoBackend.entities.MemberEntity;
import com.dripteam.innoBackend.entities.ProjectEntity;
import com.dripteam.innoBackend.repositories.DeskRepository;
import com.dripteam.innoBackend.repositories.MemberRepository;
import com.dripteam.innoBackend.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {
    private ProjectRepository projectRepository;
    private MemberRepository memberRepository;
    private DeskRepository deskRepository;

    public ProjectEntity addProject(ProjectEntity project){
        projectRepository.save(project);
        return project;
    }

    public void deleteProject(ProjectEntity project){
        projectRepository.delete(project);
    }

    public Optional<ProjectEntity> findProjectById(UUID id){
        return projectRepository.findById(id);
    }


    public  List<MemberEntity> getMembersById(UUID id){
        return memberRepository.findByProjectId(id);
    }

    public  MemberEntity addMember(MemberEntity member){
        memberRepository.save(member);
        return member;
    }

    public  void deleteMember(MemberEntity member){
        memberRepository.delete(member);
    }
    public List<ProjectEntity> findAll(){
        return projectRepository.findAll();
    }

    public List<DeskEntity> getDesksById(UUID id){
        return deskRepository.findByProjectId(id);
    }


}
