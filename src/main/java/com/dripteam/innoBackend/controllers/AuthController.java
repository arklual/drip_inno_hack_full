package com.dripteam.innoBackend.controllers;


import com.dripteam.innoBackend.entities.UserEntity;
import com.dripteam.innoBackend.entities.UserLoginSchema;
import com.dripteam.innoBackend.entities.UserRegistrationSchema;
import com.dripteam.innoBackend.entities.UserTokenSchema;
import com.dripteam.innoBackend.services.AppEmailService;
import com.dripteam.innoBackend.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;



@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;
    private AppEmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginSchema request, HttpServletResponse response) {
        Optional<UserEntity> maybe_user = userService.findUserByEmail(request.getEmail());

        if (maybe_user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn`t exits!");
        }

        UserEntity user = maybe_user.get();

        if (user.getPassword().equals(userService.hash(request.getPassword()))) {

            String token = userService.fromDataToJwt(user.getId().toString());
            UserTokenSchema tokenResponse = new UserTokenSchema();
            tokenResponse.setToken(token);

            response.setHeader("Authorization",token);

            return ResponseEntity.status(HttpStatus.OK).body(tokenResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password!");
    }

    @PostMapping("/registration")
    public ResponseEntity<Object> registration(@RequestBody UserRegistrationSchema request) {
        Optional<UserEntity> maybe_user = userService.findUserByEmail(request.getEmail());

        if (maybe_user.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exist!");
        }

        String generatedPassword = userService.generateRandomPassword();
//        System.out.println(generatedPassword);    дебаг

        UserEntity user = new UserEntity();

        user.setEmail(request.getEmail());
        user.setPassword(userService.hash(generatedPassword));
        user.setLastPasswordChange(System.currentTimeMillis());

        emailService.sendSimpleEmail(user.getEmail(), "Password: ", generatedPassword);
        userService.addUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
