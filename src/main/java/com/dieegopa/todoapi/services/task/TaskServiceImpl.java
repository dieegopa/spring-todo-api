package com.dieegopa.todoapi.services.task;

import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.exceptions.TaskNotFoundException;
import com.dieegopa.todoapi.exceptions.ForbiddenAccessException;
import com.dieegopa.todoapi.mappers.TaskMapper;
import com.dieegopa.todoapi.repositories.TaskRepository;
import com.dieegopa.todoapi.services.auth.AuthServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AuthServiceImpl authService;

    @Override
    public Iterable<TaskDto> getAllTasks() {
        var user = authService.getCurrentUser();

        return taskRepository.getAllByUser(user)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto getTaskById(long id) {
        var user = authService.getCurrentUser();
        var task = taskRepository.findById(id).orElseThrow(
                TaskNotFoundException::new
        );

        if (!task.getUser().equals(user)) {
            throw new ForbiddenAccessException();
        }

        return taskMapper.toDto(task);
    }

    @Override
    public Iterable<TaskDto> getCompletedTasks() {
        var user = authService.getCurrentUser();

        return taskRepository.getAllByUserAndCompleted(user, true)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }
}
