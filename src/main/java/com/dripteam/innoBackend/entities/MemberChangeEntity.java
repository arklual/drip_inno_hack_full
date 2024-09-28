package com.dripteam.innoBackend.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class MemberChangeEntity {
    enum TypeOfChanging {
        DELETE,
        ADD,
        UPDATE
    }
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private TypeOfChanging typeOfChanging;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId", referencedColumnName = "id")
    private ProjectEntity project;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserEntity user;

    private Role new_role;
    private Role old_role;

}
