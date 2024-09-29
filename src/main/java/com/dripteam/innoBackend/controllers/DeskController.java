package com.dripteam.innoBackend.controllers;

import com.dripteam.innoBackend.entities.*;
import com.dripteam.innoBackend.services.DeskService;
import com.dripteam.innoBackend.services.ProjectService;
import com.dripteam.innoBackend.services.TaskService;
import com.dripteam.innoBackend.services.UserService;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/desk")
@AllArgsConstructor
public class DeskController {
    private DeskService deskService;
    private UserService userService;
    private TaskService taskService;
    private ProjectService projectService;
    private EntityManager entityManager;


    @PostMapping("/")
    public ResponseEntity<Object> createDesk(@RequestBody DeskCreateSchema request, @RequestHeader(value = "Authorization",defaultValue = "None") String cookieValue) {
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
        return ResponseEntity.status(HttpStatus.CREATED).body(desk);
    }

    @DeleteMapping("/")
    public ResponseEntity<Object> deleteDesk(@RequestBody DeskDeleteSchema request, @RequestHeader(value = "Authorization",defaultValue = "None") String cookieValue) {
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
    public  ResponseEntity<Object> updateProject(@RequestBody DeskChangeDataSchema request, @RequestHeader(value = "Authorization",defaultValue = "None") String cookieValue){
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
        return ResponseEntity.ok(desk);

    }
    @GetMapping("/history")
    public List<Object> getHistory(@RequestParam String desk_id) {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);
        List<Object[]> tasks = auditReader.createQuery()
                .forRevisionsOfEntity(TaskEntity.class, false, true)
                .getResultList();

        List<Object> response = new ArrayList<>();

        for (Object[] taskRevision : tasks) {
            TaskEntity taskEntity = (TaskEntity) taskRevision[0];
            HashMap<String, String> serrTask = new HashMap<>();

            // Convert TaskEntity properties to strings and add to serrTask
            serrTask.put("id", taskEntity.getId() != null ? taskEntity.getId().toString() : null);
            serrTask.put("name", taskEntity.getName());
            serrTask.put("start", taskEntity.getStart() != null ? taskEntity.getStart().toString() : null);
            serrTask.put("end", taskEntity.getEnd() != null ? taskEntity.getEnd().toString() : null);
            serrTask.put("description", taskEntity.getDescription());
            serrTask.put("status", taskEntity.getStatus() != null ? taskEntity.getStatus().toString() : null);

            // Handle creator, worker, and desk (assume these are mapped as entities)
            serrTask.put("creator_id", taskEntity.getCreator() != null ? taskEntity.getCreator().getId().toString() : null);
            serrTask.put("worker_id", taskEntity.getWorker() != null ? taskEntity.getWorker().getId().toString() : null);
            serrTask.put("desk_id", taskEntity.getDesk() != null ? taskEntity.getDesk().getId().toString() : null);

            // Serialize RevisionType to String
            RevisionType revisionType = (RevisionType) taskRevision[2];
            serrTask.put("revision_type", revisionType != null ? revisionType.toString() : null);

            // Unproxy the revision info (DefaultRevisionEntity or custom revision entity)
            Object revisionInfo = Hibernate.unproxy(taskRevision[1]);

            if (revisionInfo instanceof DefaultRevisionEntity) {
                DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) revisionInfo;
                serrTask.put("revision_id", Integer.toString(revisionEntity.getId()));
                serrTask.put("revision_timestamp", Long.toString(revisionEntity.getTimestamp()));
            }
            List<Object> arr = new ArrayList<>();
            arr.add(serrTask);
            arr.add(revisionInfo);

            // Add to response
            response.add(arr);
        }


        return response;

    }
}
