package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.services.task.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            description = "Retrieves a list of all tasks by the authenticated user."
    )
    public Iterable<TaskDto> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID",
            description = "Retrieves a specific task by its ID for the authenticated user."
    )
    public TaskDto getTaskById(
            @Parameter(description = "ID of the task to retrieve",required = true)
            @PathVariable long id
    ) {
        return taskService.getTaskById(id);
    }

    @GetMapping("/completed")
    @Operation(summary = "Get completed tasks",
            description = "Retrieves a list of completed tasks for the authenticated user."
    )
    public Iterable<TaskDto> getCompletedTasks() {
        return taskService.getCompletedTasks();
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending tasks",
            description = "Retrieves a list of pending tasks for the authenticated user."
    )
    public Iterable<TaskDto> getPendingTasks() {
        return taskService.getPendingTasks();
    }
}
