package com.dripteam.innoBackend.entities;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskCreateSchema {
    private String name;
    private String description;
    private String deskId;
    private String workerEmail;
    private LocalDate start;
    private LocalDate end;
}
