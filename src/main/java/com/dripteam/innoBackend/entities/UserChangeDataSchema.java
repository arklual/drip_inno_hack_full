package com.dripteam.innoBackend.entities;

import lombok.Data;

import java.util.Date;
import java.util.Optional;

@Data
public class UserChangeDataSchema {
    private Optional<String> first_name = Optional.empty();
    private Optional<String> last_name = Optional.empty();
    private Optional<Date> date_of_birth = Optional.empty();
}
