package com.dripteam.innoBackend.controllers;


import com.dripteam.innoBackend.entities.UserChangeDataSchema;
import com.dripteam.innoBackend.entities.UserChangePasswordSchema;
import com.dripteam.innoBackend.entities.UserEntity;
import com.dripteam.innoBackend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/me")
@AllArgsConstructor
public class MeController {
    private UserService auth;


    @PostMapping("/change-password")
    public ResponseEntity<Object> change_password(@RequestBody UserChangePasswordSchema request, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue) {
        if (cookieValue.equals("None")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        UUID id = auth.fromJwtToId(cookieValue);
        Optional<UserEntity> maybe_user = auth.findUserById(id);

        if (maybe_user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn`t exist!");
        }

        UserEntity user = maybe_user.get();
        Date date = auth.fromJwtToTimeStamp(cookieValue);

        if (new Date(user.getLastPasswordChange()).compareTo(date) >= 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        if (user.getPassword().equals(auth.hash(request.getOld_password()))) {
            user.setPassword(auth.hash(request.getNew_password()));
            user.setLastPasswordChange(System.currentTimeMillis());
            auth.addUser(user);
            return ResponseEntity.ok("");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password!");
    }

    @PutMapping("/change-data")
    public ResponseEntity<Object> change_data(@RequestBody UserChangeDataSchema request, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue) {
        if (cookieValue.equals("None")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        UUID id = auth.fromJwtToId(cookieValue);
        Optional<UserEntity> maybe_user = auth.findUserById(id);

        if (maybe_user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn`t exist!");
        }

        UserEntity user = maybe_user.get();
        Date date = auth.fromJwtToTimeStamp(cookieValue);

        if (new Date(user.getLastPasswordChange()).compareTo(date) >= 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        if (request.getFirst_name().isPresent()) {
            user.setFirstName(request.getFirst_name().get());
        }

        if (request.getLast_name().isPresent()) {
            user.setLastName(request.getLast_name().get());
        }

        if (request.getDate_of_birth().isPresent()) {
            user.setBirthDate(request.getDate_of_birth().get());
        }

        auth.addUser(user);
        return ResponseEntity.ok("");
    }
}
