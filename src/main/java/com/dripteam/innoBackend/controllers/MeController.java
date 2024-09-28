package com.dripteam.innoBackend.controllers;


import com.dripteam.innoBackend.entities.*;
import com.dripteam.innoBackend.services.ProjectService;
import com.dripteam.innoBackend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/me")
@AllArgsConstructor
public class MeController {
    private UserService auth;
    private ProjectService projects;


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

    @PostMapping("/project")
    public ResponseEntity<Object> create_project(@RequestBody ProjectCreateSchema request, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        ProjectEntity project = new ProjectEntity();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreator(user);

        projects.addProject(project);

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @DeleteMapping("/project")
    public ResponseEntity<Object> delete_project(@RequestBody ProjectDeleteSchema request, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<ProjectEntity> maybe_project = projects.findProjectById(UUID.fromString(request.getProject_id()));

        if (maybe_project.isEmpty()) {
            return ResponseEntity.ok("");
        }
        projects.deleteProject(maybe_project.get());

        return ResponseEntity.ok("");
    }

    @PutMapping("/project")
    public  ResponseEntity<Object> update_project(@RequestBody ProjectChangeDataSchema request, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue){
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

        Optional<ProjectEntity> maybe_project = projects.findProjectById(UUID.fromString(request.getProject_id()));

        if (maybe_project.isEmpty()) {
            return ResponseEntity.ok("");
        }
        ProjectEntity project = maybe_project.get();

        if(request.getDescription().isPresent()){
            project.setDescription(request.getDescription().get());
        }

        if (request.getName().isPresent()){
            project.setName(request.getName().get());
        }

        projects.addProject(project);

        return ResponseEntity.ok("");

    }
}
