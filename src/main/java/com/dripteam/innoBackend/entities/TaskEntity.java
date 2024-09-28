package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name= "tasks")
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private UserEntity worker;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "desk_id", referencedColumnName = "id")
    private DeskEntity desk;
}
