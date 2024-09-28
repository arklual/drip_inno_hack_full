package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class ChangeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "taskId", referencedColumnName = "id")
    private TaskEntity task;

    private Status newStatus;
    private Status oldStatus;
    private String pullRequestUrl;

}

