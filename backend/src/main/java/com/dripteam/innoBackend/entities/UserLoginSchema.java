package com.dripteam.innoBackend.entities;


import lombok.Data;

@Data
public class UserLoginSchema {
    private String email;
    private String password;
}
