package com.dripteam.innoBackend.entities;

import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.envers.Audited;

import java.util.UUID;


@Entity
@Data
@Table(name= "projects")
@Audited
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private UserEntity creator;
}
