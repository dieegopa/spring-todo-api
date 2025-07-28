package com.dieegopa.todoapi.services.task;

import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.mappers.TaskMapper;
import com.dieegopa.todoapi.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Iterable<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
