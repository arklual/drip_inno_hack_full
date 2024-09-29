package com.dripteam.innoBackend.controllers;

import com.dripteam.innoBackend.entities.*;
import com.dripteam.innoBackend.services.AppEmailService;
import com.dripteam.innoBackend.services.DeskService;
import com.dripteam.innoBackend.services.TaskService;
import com.dripteam.innoBackend.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    private UserService userService;
    private DeskService deskService;
    private TaskService taskService;
    private AppEmailService emailService;

    @PostMapping("/")
    public ResponseEntity<Object> createTask(@RequestBody TaskCreateSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<DeskEntity> maybe_desk = deskService.findDeskById(UUID.fromString(request.getDeskId()));

        if (maybe_desk.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Desk not found!");
        }

        DeskEntity desk = maybe_desk.get();

        Optional<UserEntity> maybe_worker = userService.findUserByEmail(request.getWorkerEmail());

        if (maybe_worker.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found1");
        }

        UserEntity worker = maybe_worker.get();

        TaskEntity task = new TaskEntity();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setStatus(Status.NOT_STARTED);
        task.setCreator(user);
        task.setDesk(desk);
        task.setWorker(worker);
        task.setStart(request.getStart());
        task.setEnd(request.getEnd());

        taskService.addTask(task);
        emailService.sendSimpleEmail(request.getWorkerEmail(), "Новая задача:", "Владелец проекта " + user.getEmail() + " поставил вам задачу " + task.getName() + " на доске " + desk.getTitle() + ".\nУдачного выполнения!");

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @DeleteMapping("/")
    public ResponseEntity<Object> deleteTask(@RequestBody TaskDeleteSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<TaskEntity> maybe_task = taskService.getById(UUID.fromString(request.getTaskId()));

        if (maybe_task.isEmpty()) {
            return ResponseEntity.ok("");
        }

        TaskEntity task = maybe_task.get();

        taskService.deleteTask(task);

        return ResponseEntity.ok("");
    }

    public ResponseEntity<Object> updateTask(@RequestBody TaskUpdateSchema request, @RequestHeader(value = "Authorization", defaultValue = "None") String cookieValue) {
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

        Optional<TaskEntity> maybe_task = taskService.getById(UUID.fromString(request.getTaskId()));

        if (maybe_task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found!");
        }

        TaskEntity task = maybe_task.get();

        if (request.getName().isPresent()) {
            task.setName(request.getName().get());
        }

        if (request.getDescription().isPresent()) {
            task.setDescription(request.getDescription().get());
        }

        if (request.getStatus().isPresent()) {
            task.setStatus(request.getStatus().get());
        }

        taskService.addTask(task);

        return ResponseEntity.ok(task);

    }
}
