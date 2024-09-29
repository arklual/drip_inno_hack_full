package com.dripteam.innoBackend.services;

import com.dripteam.innoBackend.entities.InvitationEntity;
import com.dripteam.innoBackend.repositories.InvitationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class InvitationService {
    private InvitationRepository invitationRepository;

    public InvitationEntity addInvitation(InvitationEntity invitation){
        invitationRepository.save(invitation);
        return invitation;
    }

    public Optional<InvitationEntity> getById(UUID id){
        return invitationRepository.findById(id);
    }

    public void deleteInvitation(InvitationEntity invitation){
        invitationRepository.delete(invitation);
    }
}
