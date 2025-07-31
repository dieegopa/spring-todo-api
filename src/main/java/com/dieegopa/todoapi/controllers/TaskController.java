package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.dtos.CreateTaskRequest;
import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.services.task.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
            @Parameter(description = "ID of the task to retrieve", required = true)
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

    @PostMapping
    @Operation(summary = "Create a new task",
            description = "Creates a new task for the authenticated user."
    )
    public TaskDto createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }
}
