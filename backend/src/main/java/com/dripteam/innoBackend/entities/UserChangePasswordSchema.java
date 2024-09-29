package com.dripteam.innoBackend.entities;

import lombok.Data;

@Data
public class UserChangePasswordSchema {
    private String old_password;
    private String new_password;
}
