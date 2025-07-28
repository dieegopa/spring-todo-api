package com.dieegopa.todoapi.services.task;

import com.dieegopa.todoapi.dtos.TaskDto;

public interface ITaskService {
    Iterable<TaskDto> getAllTasks();
}
