package com.dripteam.innoBackend.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import java.util.UUID;

import java.util.Date;


@Data
@Entity
@Table(name= "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique=true)
    @Email
    private String email;
    private String password;
    private String avatarURL;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private long lastPasswordChange;
}
