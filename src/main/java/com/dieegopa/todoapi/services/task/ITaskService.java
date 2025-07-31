package com.dieegopa.todoapi.services.task;

import com.dieegopa.todoapi.dtos.CreateTaskRequest;
import com.dieegopa.todoapi.dtos.TaskDto;

public interface ITaskService {
    Iterable<TaskDto> getAllTasks();

    TaskDto getTaskById(long id);

    Iterable<TaskDto> getCompletedTasks();

    Iterable<TaskDto> getPendingTasks();

    TaskDto createTask(CreateTaskRequest request);
}
