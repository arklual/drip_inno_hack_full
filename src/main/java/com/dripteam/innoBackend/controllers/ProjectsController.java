package com.dripteam.innoBackend.controllers;

import com.dripteam.innoBackend.entities.*;
import com.dripteam.innoBackend.services.AppEmailService;
import com.dripteam.innoBackend.services.InvitationService;
import com.dripteam.innoBackend.services.ProjectService;
import com.dripteam.innoBackend.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@AllArgsConstructor
@RequestMapping("/project")
public class ProjectsController {
    private AppEmailService emailService;
    private ProjectService projectService;
    private UserService userService;
    private InvitationService invitationService;

    @PostMapping("/invite/{project_id}")
    public ResponseEntity<Object> inviteToProject(@PathVariable String project_id, @RequestBody ProjectInviteSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<ProjectEntity> maybe_project = projectService.findProjectById(UUID.fromString(project_id));

        if (maybe_project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found!");
        }

        ProjectEntity project = maybe_project.get();

        if (!user.getId().equals(project.getCreator().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your not creator!");
        }

        Optional<UserEntity> maybe_getter = userService.findUserByEmail(request.getEmail());

        if (maybe_getter.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Getter not found!");
        }

        UserEntity getter = maybe_getter.get();

        InvitationEntity invitation = new InvitationEntity();
        invitation.setUser(getter);
        invitation.setProject(project);


        invitationService.addInvitation(invitation);

        String link = "https://localhost:5137/" + invitation.getId();
        emailService.sendSimpleEmail(request.getEmail(), "Invite link:", link);

        return ResponseEntity.ok("");
    }

    @PostMapping("/apply-invite/{uuid}")
    public ResponseEntity<Object> applyInvite(@PathVariable String uuid, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<InvitationEntity> maybe_invitation = invitationService.getById(UUID.fromString(uuid));

        if (maybe_invitation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invitation not found!");
        }

        InvitationEntity invitation = maybe_invitation.get();

        MemberEntity member = new MemberEntity();
        member.setRole(Role.EDITOR);
        member.setProject(invitation.getProject());
        member.setUser(invitation.getUser());

        projectService.addMember(member);
        invitationService.deleteInvitation(invitation);

        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @DeleteMapping("/remove/{project_id}")
    public ResponseEntity<Object> deleteFromProject(@PathVariable String project_id, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        List<MemberEntity> members = projectService.getMembersById(UUID.fromString(project_id));

        MemberEntity find = null;

        for (MemberEntity member : members) {
            if (member.getUser().getId().equals(user.getId())) {
                find = member;
                break;
            }
        }

        if (find != null) {
            projectService.deleteMember(find);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Removed!");
    }

    @GetMapping("/{project_id}")
    public ResponseEntity<Object> getProject(@PathVariable String project_id, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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
        Optional<ProjectEntity> maybe_project = projectService.findProjectById(UUID.fromString(project_id));
        List<DeskEntity> desks = projectService.getDesksById(UUID.fromString(project_id));
        List<MemberEntity> members = projectService.getMembersById(UUID.fromString(project_id));

        if (maybe_project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found!");
        }
        ProjectEntity project = maybe_project.get();
        @Data
        class Schema {
            public UUID id;
            public String name;
            public String description;
            public UserEntity creator;
            public List<DeskEntity> desks;
            public List<MemberEntity> members;
        }
        Schema response = new Schema();
        response.setId(UUID.fromString(project_id));
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setCreator(user);
        response.setDesks(desks);
        response.setMembers(members);

        return ResponseEntity.ok(response);
    }


}
