package com.dripteam.innoBackend.services;

import com.dripteam.innoBackend.entities.TaskEntity;
import com.dripteam.innoBackend.repositories.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class TaskService {
    private TaskRepository taskRepository;

    public TaskEntity addTask(TaskEntity task){
        taskRepository.save(task);
        return task;
    }

    public Optional<TaskEntity> getById(UUID id){
        return taskRepository.findById(id);
    }

    public void deleteTask(TaskEntity task){
        taskRepository.delete(task);
    }

}
