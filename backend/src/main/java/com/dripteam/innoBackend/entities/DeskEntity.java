package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.util.UUID;


@Entity
@Data
@Table(name = "desks")
@Audited
public class DeskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private ProjectEntity project;
}
