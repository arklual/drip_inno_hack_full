package com.dripteam.innoBackend.controllers;


import com.dripteam.innoBackend.entities.*;
import com.dripteam.innoBackend.services.ProjectService;
import com.dripteam.innoBackend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/me")
@AllArgsConstructor
public class MeController {
    private UserService userService;
    private ProjectService projectService;

    @GetMapping("/")
    public ResponseEntity<Object> getMe(@RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        return ResponseEntity.ok(user);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody UserChangePasswordSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        if (user.getPassword().equals(userService.hash(request.getOld_password()))) {
            user.setPassword(userService.hash(request.getNew_password()));
            user.setLastPasswordChange(System.currentTimeMillis());
            userService.addUser(user);
            return ResponseEntity.ok("");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password!");
    }

    @PutMapping("/change-data")
    public ResponseEntity<Object> changeData(@RequestBody UserChangeDataSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        if (request.getFirst_name().isPresent()) {
            user.setFirstName(request.getFirst_name().get());
        }

        if (request.getLast_name().isPresent()) {
            user.setLastName(request.getLast_name().get());
        }

        if (request.getDate_of_birth().isPresent()) {
            user.setBirthDate(request.getDate_of_birth().get());
        }

        userService.addUser(user);
        return ResponseEntity.ok("");
    }

    @PostMapping("/project")
    public ResponseEntity<Object> createProject(@RequestBody ProjectCreateSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        ProjectEntity project = new ProjectEntity();
        MemberEntity member = new MemberEntity();

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreator(user);

        projectService.addProject(project);

        member.setProject(project);
        member.setUser(user);
        member.setRole(Role.OWNER);

        projectService.addMember(member);

        return ResponseEntity.status(HttpStatus.CREATED).body(project);
    }

    @DeleteMapping("/project")
    public ResponseEntity<Object> deleteProject(@RequestBody ProjectDeleteSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<ProjectEntity> maybe_project = projectService.findProjectById(UUID.fromString(request.getProject_id()));

        if (maybe_project.isEmpty()) {
            return ResponseEntity.ok("");
        }
        projectService.deleteProject(maybe_project.get());

        return ResponseEntity.ok("");
    }

    @PutMapping("/project")
    public ResponseEntity<Object> updateProject(@RequestBody ProjectChangeDataSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<ProjectEntity> maybe_project = projectService.findProjectById(UUID.fromString(request.getProject_id()));

        if (maybe_project.isEmpty()) {
            return ResponseEntity.ok("");
        }
        ProjectEntity project = maybe_project.get();

        if (request.getDescription().isPresent()) {
            project.setDescription(request.getDescription().get());
        }

        if (request.getName().isPresent()) {
            project.setName(request.getName().get());
        }

        projectService.addProject(project);

        return ResponseEntity.ok(project);

    }

    @GetMapping("/project")
    public ResponseEntity<Object> myProjects(@RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        List<ProjectEntity> all_projects = projectService.findAll();
        List<ProjectEntity> needed_projects = new ArrayList<>();
        for (var project : all_projects) {
            List<MemberEntity> members = projectService.getMembersById(project.getId());
            boolean find = false;
            for (MemberEntity member : members) {
                if (member.getUser().getId().equals(user.getId())) {
                    find = true;
                    break;
                }
            }
            if (find) {
                needed_projects.add(project);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(needed_projects);
    }

}
