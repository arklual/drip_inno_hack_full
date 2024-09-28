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
    @Column(unique=true,name = "email")
    @Email
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "avatarURL")
    private String avatarURL;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "birthDate")
    private Date birthDate;
    @Column(name = "lastPasswordChange")
    private long lastPasswordChange;
}
