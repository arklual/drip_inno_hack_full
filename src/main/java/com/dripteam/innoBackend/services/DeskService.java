package com.dripteam.innoBackend.services;

import com.dripteam.innoBackend.entities.DeskEntity;
import com.dripteam.innoBackend.entities.ProjectEntity;
import com.dripteam.innoBackend.repositories.DeskRepository;
import com.dripteam.innoBackend.repositories.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DeskService {
    private DeskRepository repository;

    public DeskEntity addDesk(DeskEntity desk){
        repository.save(desk);
        return desk;
    }

    public void deleteDesk(DeskEntity desk){
        repository.delete(desk);
    }

    public Optional<DeskEntity> findDeskById(UUID id){
        return repository.findById(id);
    }
}
