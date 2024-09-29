package com.dripteam.innoBackend.entities;


import lombok.Data;

import java.util.Optional;

@Data
public class TaskUpdateSchema {
    private String taskId;
    private Optional<Status> status = Optional.empty();
    private Optional<String> name = Optional.empty();
    private Optional<String> description = Optional.empty();
}
