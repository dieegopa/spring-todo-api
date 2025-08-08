package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.TaskRequest;
import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.entities.Task;
import com.dieegopa.todoapi.entities.User;
import com.dieegopa.todoapi.exceptions.ForbiddenAccessException;
import com.dieegopa.todoapi.exceptions.TaskNotFoundException;
import com.dieegopa.todoapi.repositories.TagRepository;
import com.dieegopa.todoapi.repositories.TaskRepository;
import com.dieegopa.todoapi.repositories.UserRepository;
import com.dieegopa.todoapi.services.task.ITaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
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

    @Autowired
    private TagRepository tagRepository;

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
                .tags(new HashSet<>())
                .build();

        taskRepository.save(task);

        List<TaskDto> tasks = (List<TaskDto>) taskService.getAllTasks();

        System.out.println(task.getStartDatetime().truncatedTo(ChronoUnit.SECONDS));

        assertFalse(tasks.isEmpty());
        assertTrue(tasks.stream().anyMatch(t -> t.getName().equals(task.getName())));
        assertTrue(tasks.stream().anyMatch(t -> t.getDescription().equals(task.getDescription())));
        System.out.println(task.getStartDatetime().truncatedTo(ChronoUnit.SECONDS));
        assertTrue(tasks.stream().anyMatch(t -> t.getStartDatetime().truncatedTo(ChronoUnit.SECONDS).equals(task.getStartDatetime().truncatedTo(ChronoUnit.SECONDS))));

    }

    @Test
    public void testGetTaskById() {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .tags(new HashSet<>())
                .build();

        taskRepository.save(task);

        TaskDto taskDto = taskService.getTaskById(task.getId());

        assertNotNull(taskDto);
        assertEquals(task.getName(), taskDto.getName());
        assertEquals(task.getDescription(), taskDto.getDescription());
        assertEquals(task.getStartDatetime().truncatedTo(ChronoUnit.SECONDS), taskDto.getStartDatetime().truncatedTo(ChronoUnit.SECONDS));
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
                .tags(new HashSet<>())
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
                .tags(new HashSet<>())
                .build();

        Task task2 = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .tags(new HashSet<>())
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
                .tags(new HashSet<>())
                .build();

        Task task2 = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(true)
                .user(user)
                .tags(new HashSet<>())
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
        TaskRequest newTask = TaskRequest.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .tags(new HashSet<>())
                .build();

        TaskDto createdTask = taskService.createTask(newTask);

        assertNotNull(createdTask);
        assertEquals(newTask.getName(), createdTask.getName());
        assertEquals(newTask.getDescription(), createdTask.getDescription());
        assertEquals(newTask.getStartDatetime(), createdTask.getStartDatetime());
        assertFalse(createdTask.isCompleted());
    }

    @Test
    public void testCreateTaskWithTags() {
        Tag tag1 = new Tag();
        tag1.setName(faker.lorem().word());
        tag1.setUser(user);
        tagRepository.save(tag1);

        TaskRequest newTask = TaskRequest.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .tags(new HashSet<>(List.of(tag1.getId())))
                .build();

        TaskDto createdTask = taskService.createTask(newTask);

        assertNotNull(createdTask);
        assertEquals(newTask.getName(), createdTask.getName());
        assertEquals(newTask.getDescription(), createdTask.getDescription());
        assertEquals(newTask.getStartDatetime(), createdTask.getStartDatetime());
        assertFalse(createdTask.isCompleted());
        assertFalse(createdTask.getTags().isEmpty());
        assertTrue(createdTask.getTags().stream().anyMatch(tag -> tag.getId().equals(tag1.getId())));
    }

    @Test
    public void testUpdateTask() {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .tags(new HashSet<>())
                .build();

        taskRepository.save(task);

        TaskRequest updateRequest = TaskRequest.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now().plusDays(1))
                .completed(true)
                .tags(new HashSet<>())
                .build();

        TaskDto updatedTask = taskService.updateTask(task.getId(), updateRequest);

        assertNotNull(updatedTask);
        assertEquals(updateRequest.getName(), updatedTask.getName());
        assertEquals(updateRequest.getDescription(), updatedTask.getDescription());
        assertEquals(updateRequest.getStartDatetime(), updatedTask.getStartDatetime());
        assertTrue(updatedTask.isCompleted());
    }

    @Test
    public void testUpdateTaskWithTags() {
        Tag tag1 = new Tag();
        tag1.setName(faker.lorem().word());
        tag1.setUser(user);
        tagRepository.save(tag1);

        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .tags(new HashSet<>())
                .build();

        taskRepository.save(task);

        TaskRequest updateRequest = TaskRequest.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now().plusDays(1))
                .completed(true)
                .tags(new HashSet<>(List.of(tag1.getId())))
                .build();

        TaskDto updatedTask = taskService.updateTask(task.getId(), updateRequest);

        assertNotNull(updatedTask);
        assertEquals(updateRequest.getName(), updatedTask.getName());
        assertEquals(updateRequest.getDescription(), updatedTask.getDescription());
        assertEquals(updateRequest.getStartDatetime(), updatedTask.getStartDatetime());
        assertTrue(updatedTask.isCompleted());
        assertFalse(updatedTask.getTags().isEmpty());
        assertTrue(updatedTask.getTags().stream().anyMatch(tag -> tag.getId().equals(tag1.getId())));
    }

    @Test
    public void testUpdateTaskWithTagsRemovesOldOnes(){
        Tag tag1 = new Tag();
        tag1.setName(faker.lorem().word());
        tag1.setUser(user);
        tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName(faker.lorem().word());
        tag2.setUser(user);
        tagRepository.save(tag2);

        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .tags(new HashSet<>(List.of(tag1)))
                .build();

        taskRepository.save(task);

        TaskRequest updateRequest = TaskRequest.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now().plusDays(1))
                .completed(true)
                .tags(new HashSet<>(List.of(tag2.getId())))
                .build();

        TaskDto updatedTask = taskService.updateTask(task.getId(), updateRequest);

        assertNotNull(updatedTask);
        assertEquals(updateRequest.getName(), updatedTask.getName());
        assertEquals(updateRequest.getDescription(), updatedTask.getDescription());
        assertEquals(updateRequest.getStartDatetime(), updatedTask.getStartDatetime());
        assertTrue(updatedTask.isCompleted());
        assertFalse(updatedTask.getTags().isEmpty());
        assertTrue(updatedTask.getTags().stream().anyMatch(tag -> tag.getId().equals(tag2.getId())));
        assertFalse(updatedTask.getTags().stream().anyMatch(tag -> tag.getId().equals(tag1.getId())));
    }

    @Test
    public void testDeleteTask() {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .tags(new HashSet<>())
                .build();

        taskRepository.save(task);

        taskService.deleteTask(task.getId());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(task.getId()));
    }

    @Test
    public void testDeleteTaskForbiddenAccess() {
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
                .tags(new HashSet<>())
                .build();

        taskRepository.save(task);

        assertThrows(ForbiddenAccessException.class, () -> taskService.deleteTask(task.getId()));
    }

    @Test
    public void testDeleteTaskNotFound() {
        long nonExistentTaskId = 9999L;

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(nonExistentTaskId));
    }
}
