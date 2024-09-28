package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "project_changes")
public class ProjectChangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;

    @Column(name = "newName")
    private String newName;
    @Column(name = "oldName")
    private String oldName;
    @Column(name = "new_description")
    private String new_description;
    @Column(name = "old_description")
    private String old_description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oldUser_id", referencedColumnName = "id")
    private UserEntity old_user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "newUser_id", referencedColumnName = "id")
    private UserEntity new_user;


}

