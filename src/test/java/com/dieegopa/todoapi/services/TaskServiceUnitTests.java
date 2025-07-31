package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.CreateTaskRequest;
import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.entities.Task;
import com.dieegopa.todoapi.entities.User;
import com.dieegopa.todoapi.exceptions.ForbiddenAccessException;
import com.dieegopa.todoapi.exceptions.TaskNotFoundException;
import com.dieegopa.todoapi.repositories.TaskRepository;
import com.dieegopa.todoapi.repositories.UserRepository;
import com.dieegopa.todoapi.services.task.ITaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class TaskServiceUnitTests extends BaseTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ITaskService taskService;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mockSecurityContext();
    }

    @Test
    public void testGetAllTasks() {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .build();

        taskRepository.save(task);

        List<TaskDto> tasks = (List<TaskDto>) taskService.getAllTasks();

        System.out.println(task.getStartDatetime().truncatedTo(ChronoUnit.SECONDS));

        assertFalse(tasks.isEmpty());
        assertTrue(tasks.stream().anyMatch(t -> t.getName().equals(task.getName())));
        assertTrue(tasks.stream().anyMatch(t -> t.getDescription().equals(task.getDescription())));
        assertTrue(tasks.stream().anyMatch(t -> t.getStartDatetime().equals(task.getStartDatetime())));

    }

    @Test
    public void testGetTaskById() {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .build();

        taskRepository.save(task);

        TaskDto taskDto = taskService.getTaskById(task.getId());

        assertNotNull(taskDto);
        assertEquals(task.getName(), taskDto.getName());
        assertEquals(task.getDescription(), taskDto.getDescription());
        assertEquals(task.getStartDatetime(), taskDto.getStartDatetime());
    }

    @Test
    public void testGetTaskByIdNotFound() {
        long nonExistentTaskId = 9999L;

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(nonExistentTaskId));
    }

    @Test
    public void testGetTaskByIdForbiddenAccess() {
        User otherUser = User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();

        userRepository.save(otherUser);

        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(otherUser)
                .build();

        taskRepository.save(task);

        assertThrows(ForbiddenAccessException.class, () -> taskService.getTaskById(task.getId()));

    }

    @Test
    public void testGetCompletedTasks() {
        Task task1 = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(true)
                .user(user)
                .build();

        Task task2 = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .build();

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<TaskDto> completedTasks = (List<TaskDto>) taskService.getCompletedTasks();

        assertFalse(completedTasks.isEmpty());
        assertTrue(completedTasks.stream().anyMatch(t -> t.getName().equals(task1.getName())));
        assertFalse(completedTasks.stream().anyMatch(t -> t.getName().equals(task2.getName())));
    }

    @Test
    public void testGetPendingTasks() {
        Task task1 = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .build();

        Task task2 = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(true)
                .user(user)
                .build();

        taskRepository.save(task1);
        taskRepository.save(task2);

        List<TaskDto> pendingTasks = (List<TaskDto>) taskService.getPendingTasks();

        assertFalse(pendingTasks.isEmpty());
        assertTrue(pendingTasks.stream().anyMatch(t -> t.getName().equals(task1.getName())));
        assertFalse(pendingTasks.stream().anyMatch(t -> t.getName().equals(task2.getName())));
    }

    @Test
    public void testCreateTask() {
        CreateTaskRequest newTask = CreateTaskRequest.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .build();

        TaskDto createdTask = taskService.createTask(newTask);

        assertNotNull(createdTask);
        assertEquals(newTask.getName(), createdTask.getName());
        assertEquals(newTask.getDescription(), createdTask.getDescription());
        assertEquals(newTask.getStartDatetime(), createdTask.getStartDatetime());
        assertFalse(createdTask.isCompleted());
    }
}
