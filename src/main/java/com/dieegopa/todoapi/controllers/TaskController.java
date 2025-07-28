package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.services.task.ITaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final ITaskService taskService;

    @GetMapping
    public Iterable<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }
}
