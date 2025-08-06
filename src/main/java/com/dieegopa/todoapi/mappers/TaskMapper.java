package com.dieegopa.todoapi.mappers;

import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.dtos.TaskRequest;
import com.dieegopa.todoapi.entities.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "tags", ignore = true)
    })
    Task toEntity(TaskRequest request);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "user", ignore = true),
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "tags", ignore = true)
    })
    void updateEntityFromRequest(TaskRequest request, @MappingTarget Task task);
}
