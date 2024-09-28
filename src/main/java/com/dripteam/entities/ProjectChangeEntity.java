package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class ProjectChangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId", referencedColumnName = "id")
    private ProjectEntity project;

    private String newName;
    private String oldName;
    private String new_description;
    private String old_description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oldUserId", referencedColumnName = "id")
    private UserEntity old_user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "newUserId", referencedColumnName = "id")
    private UserEntity new_user;


}

