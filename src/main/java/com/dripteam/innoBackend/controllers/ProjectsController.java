package com.dripteam.innoBackend.controllers;

import com.dripteam.innoBackend.entities.*;
import com.dripteam.innoBackend.services.AppEmailService;
import com.dripteam.innoBackend.services.ProjectService;
import com.dripteam.innoBackend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectsController {
    private AppEmailService notification;
    private ProjectService service;
    private UserService auth;

    @PostMapping("/invite/{project_id}")
    public ResponseEntity<Object> inviteToProject(@PathVariable String project_id, @RequestBody ProjectInviteSchema request, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<ProjectEntity> maybe_project = service.findProjectById(UUID.fromString(project_id));

        if (maybe_project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found!");
        }

        ProjectEntity project = maybe_project.get();

        if (!user.getId().equals(project.getCreator().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your not creator!");
        }
        String link = "https://aa28-212-46-226-137.ngrok-free.app/projects/join/" + project_id;
        notification.sendSimpleEmail(request.getEmail(), "Invite link:", link);

        return ResponseEntity.ok("");
    }

    @GetMapping("/join/{project_id}")
    public ResponseEntity<Object> joinToProject(@PathVariable String project_id, @CookieValue(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        List<MemberEntity> members = service.getMembersById(UUID.fromString(project_id));
        Optional<ProjectEntity> maybe_project = service.findProjectById(UUID.fromString(project_id));

        if (maybe_project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found!");
        }

        ProjectEntity project = maybe_project.get();

        boolean find = false;

        for (MemberEntity member : members) {
            if (member.getUser().getId().equals(user.getId())) {
                find = true;
                break;
            }
        }

        if (!find) {
            MemberEntity new_member = new MemberEntity();
            new_member.setRole(Role.EDITOR);
            new_member.setUser(user);
            new_member.setProject(project);
            service.addMember(new_member);
        }

        return ResponseEntity.ok("");

    }
}
