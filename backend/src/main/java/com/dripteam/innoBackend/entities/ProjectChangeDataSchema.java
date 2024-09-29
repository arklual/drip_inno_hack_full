package com.dripteam.innoBackend.entities;

import lombok.Data;

import java.util.Optional;

@Data
public class ProjectChangeDataSchema {
    private String project_id;
    private Optional<String> name = Optional.empty();
    private Optional<String> description = Optional.empty();
}


