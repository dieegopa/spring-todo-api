package com.dieegopa.todoapi.services.task;

import com.dieegopa.todoapi.dtos.TaskRequest;
import com.dieegopa.todoapi.dtos.TaskDto;

public interface ITaskService {
    Iterable<TaskDto> getAllTasks();

    TaskDto getTaskById(long id);

    Iterable<TaskDto> getCompletedTasks();

    Iterable<TaskDto> getPendingTasks();

    TaskDto createTask(TaskRequest request);

    TaskDto updateTask(Long id, TaskRequest request);

    void deleteTask(Long id);
}
