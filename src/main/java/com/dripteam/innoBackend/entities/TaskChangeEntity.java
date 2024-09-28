package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "task_changes")
public class TaskChangeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private TaskEntity task;

    @Column(name = "oldName")
    private String oldName;
    @Column(name = "newName")
    private String newName;
    @Column(name = "oldDescription")
    private String oldDescription;
    @Column(name = "newDescription")
    private String newDescription;
}
