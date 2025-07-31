package com.dieegopa.todoapi.mappers;

import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.dtos.TaskRequest;
import com.dieegopa.todoapi.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);

    Task toEntity(TaskRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(TaskRequest request, @MappingTarget Task task);
}
