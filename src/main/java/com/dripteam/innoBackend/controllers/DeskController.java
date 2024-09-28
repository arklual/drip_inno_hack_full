package com.dripteam.innoBackend.controllers;

import com.dripteam.innoBackend.entities.*;
import com.dripteam.innoBackend.services.DeskService;
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
@RequestMapping("/desk")
@AllArgsConstructor
public class DeskController {
    private DeskService deskService;
    private UserService auth;
    private ProjectService projectService;

    @PostMapping("/")
    public ResponseEntity<Object> create_desk(@RequestBody DeskCreateSchema request, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        DeskEntity desk = new DeskEntity();
        desk.setTitle(request.getName());
        Optional<ProjectEntity> maybe_project = projectService.findProjectById(UUID.fromString(request.getProjectId()));
        if (maybe_project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project doesn`t exist!");
        }
        desk.setProject(maybe_project.get());
        deskService.addDesk(desk);
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

    @DeleteMapping("/")
    public ResponseEntity<Object> delete_desk(@RequestBody DeskDeleteSchema request, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<DeskEntity> maybe_desk = deskService.findDeskById(UUID.fromString(request.getDesk_id()));

        if (maybe_desk.isEmpty()) {
            return ResponseEntity.ok("");
        }
        deskService.deleteDesk(maybe_desk.get());

        return ResponseEntity.ok("");
    }
}
