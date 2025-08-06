package com.dieegopa.todoapi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TaskRequest {

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Completed status cannot be null")
    private boolean completed;

    @NotNull(message = "Start datetime cannot be null")
    private LocalDateTime startDatetime;

    private LocalDateTime endDatetime;

    private Set<Long> tags;
}
