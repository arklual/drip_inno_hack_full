package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "status_changes")
public class ChangeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private TaskEntity task;

    @Column(name = "newStatus")
    private Status newStatus;
    @Column(name = "oldStatus")
    private Status oldStatus;
    @Column(name = "pullRequestUrl")
    private String pullRequestUrl;

}

