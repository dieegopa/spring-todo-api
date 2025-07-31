package com.dieegopa.todoapi.mappers;

import com.dieegopa.todoapi.dtos.CreateTaskRequest;
import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.entities.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);

    Task toEntity(CreateTaskRequest request);
}
