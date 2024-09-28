package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class TaskChangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private TaskEntity task;

    private String oldName;
    private String newName;
    private String oldDescription;
    private String newDescription;
}
