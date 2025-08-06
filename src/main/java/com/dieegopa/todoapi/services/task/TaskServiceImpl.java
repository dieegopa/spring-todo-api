package com.dieegopa.todoapi.services.task;

import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.dtos.TaskRequest;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.exceptions.ForbiddenAccessException;
import com.dieegopa.todoapi.exceptions.TaskNotFoundException;
import com.dieegopa.todoapi.mappers.TaskMapper;
import com.dieegopa.todoapi.repositories.TaskRepository;
import com.dieegopa.todoapi.services.auth.AuthServiceImpl;
import com.dieegopa.todoapi.services.tag.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final AuthServiceImpl authService;
    private final ITagService tagService;

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

        if (!task.getUser().getId().equals(user.getId())) {
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

    @Override
    public Iterable<TaskDto> getPendingTasks() {
        var user = authService.getCurrentUser();

        return taskRepository.getAllByUserAndCompleted(user, false)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto createTask(TaskRequest request) {
        var user = authService.getCurrentUser();
        var task = taskMapper.toEntity(request);
        task.setUser(user);

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            Set<Tag> userTags = tagService.validateAndGetTagsForUser(request.getTags(), user);
            task.setTags(userTags);
        }

        var savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskDto updateTask(Long id, TaskRequest request) {
        var user = authService.getCurrentUser();
        var task = taskRepository.findById(id).orElseThrow(
                TaskNotFoundException::new
        );

        if (!task.getUser().getId().equals(user.getId())) {
            throw new ForbiddenAccessException();
        }

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            Set<Tag> userTags = tagService.validateAndGetTagsForUser(request.getTags(), user);
            task.setTags(userTags);
        }

        taskMapper.updateEntityFromRequest(request, task);
        var updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    public void deleteTask(Long id) {
        var user = authService.getCurrentUser();
        var task = taskRepository.findById(id).orElseThrow(
                TaskNotFoundException::new
        );

        if (!task.getUser().getId().equals(user.getId())) {
            throw new ForbiddenAccessException();
        }

        taskRepository.delete(task);
    }
}
