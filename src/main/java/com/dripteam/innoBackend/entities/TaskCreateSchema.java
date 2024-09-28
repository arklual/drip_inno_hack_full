package com.dripteam.innoBackend.entities;

import lombok.Data;

@Data
public class TaskCreateSchema {
    private String name;
    private String description;
    private String deskId;
    private String workerEmail;
}
