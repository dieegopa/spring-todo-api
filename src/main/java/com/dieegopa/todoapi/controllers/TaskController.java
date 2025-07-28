package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.services.task.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Task management operations")
public class TaskController {

    private final ITaskService taskService;

    @GetMapping
    @Operation(summary = "Get all tasks",
            description = "Retrieves a list of all tasks in the system."
    )
    public Iterable<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }
}
