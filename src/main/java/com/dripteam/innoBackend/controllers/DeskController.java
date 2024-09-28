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


@RestController
@RequestMapping("/desk")
@AllArgsConstructor
public class DeskController {
    private DeskService deskService;
    private UserService userService;
    private ProjectService projectService;

    @PostMapping("/")
    public ResponseEntity<Object> create_desk(@RequestBody DeskCreateSchema request, @RequestHeader(value = "Authorization",defaultValue = "None") String cookieValue) {
        if (cookieValue.equals("None")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        UUID id = userService.fromJwtToId(cookieValue);
        Optional<UserEntity> maybe_user = userService.findUserById(id);

        if (maybe_user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn`t exist!");
        }

        UserEntity user = maybe_user.get();
        Date date = userService.fromJwtToTimeStamp(cookieValue);

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
    public ResponseEntity<Object> delete_desk(@RequestBody DeskDeleteSchema request, @RequestHeader(value = "Authorization",defaultValue = "None") String cookieValue) {
        if (cookieValue.equals("None")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        UUID id = userService.fromJwtToId(cookieValue);
        Optional<UserEntity> maybe_user = userService.findUserById(id);

        if (maybe_user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn`t exist!");
        }

        UserEntity user = maybe_user.get();
        Date date = userService.fromJwtToTimeStamp(cookieValue);

        if (new Date(user.getLastPasswordChange()).compareTo(date) >= 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        Optional<DeskEntity> maybe_desk = deskService.findDeskById(UUID.fromString(request.getDesk_id()));

        if (maybe_desk.isEmpty()) {
            return ResponseEntity.ok("");
        }

        DeskEntity desk = maybe_desk.get();
        deskService.deleteDesk(desk);

        return ResponseEntity.ok("");
    }

    @PutMapping("/")
    public  ResponseEntity<Object> update_project(@RequestBody DeskChangeDataSchema request, @RequestHeader(value = "Authorization",defaultValue = "None") String cookieValue){
        if (cookieValue.equals("None")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        UUID id = userService.fromJwtToId(cookieValue);
        Optional<UserEntity> maybe_user = userService.findUserById(id);

        if (maybe_user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User doesn`t exist!");
        }

        UserEntity user = maybe_user.get();
        Date date = userService.fromJwtToTimeStamp(cookieValue);

        if (new Date(user.getLastPasswordChange()).compareTo(date) >= 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token!");
        }

        Optional<DeskEntity> maybe_desk = deskService.findDeskById(UUID.fromString(request.getDesk_id()));

        if (maybe_desk.isEmpty()) {
            return ResponseEntity.ok("");
        }
        DeskEntity desk = maybe_desk.get();
        desk.setTitle(request.getName());
        deskService.addDesk(desk);
        return ResponseEntity.ok("");

    }
}
