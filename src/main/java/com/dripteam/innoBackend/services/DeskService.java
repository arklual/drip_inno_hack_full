package com.dripteam.innoBackend.services;

import com.dripteam.innoBackend.entities.DeskEntity;
import com.dripteam.innoBackend.repositories.DeskRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class DeskService {
    private DeskRepository deskRepository;

    public DeskEntity addDesk(DeskEntity desk) {
        deskRepository.save(desk);
        return desk;
    }

    public void deleteDesk(DeskEntity desk) {
        deskRepository.delete(desk);
    }

    public Optional<DeskEntity> findDeskById(UUID id) {
        return deskRepository.findById(id);
    }
}
