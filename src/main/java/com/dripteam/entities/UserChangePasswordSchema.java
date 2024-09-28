package com.dripteam.innoBackend.entities;

import lombok.Data;

@Data
public class UserChangePasswordSchema {
    public String old_password;
    public String new_password;
}
