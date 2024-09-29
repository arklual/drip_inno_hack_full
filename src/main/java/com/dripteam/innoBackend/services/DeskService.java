package com.dripteam.innoBackend.services;

import com.dripteam.innoBackend.entities.DeskEntity;
import com.dripteam.innoBackend.repositories.DeskRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
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
