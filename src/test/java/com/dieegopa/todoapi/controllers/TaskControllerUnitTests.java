package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.CreateTaskRequest;
import com.dieegopa.todoapi.dtos.TaskDto;
import com.dieegopa.todoapi.entities.Task;
import com.dieegopa.todoapi.entities.User;
import com.dieegopa.todoapi.repositories.TaskRepository;
import com.dieegopa.todoapi.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerUnitTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void testGetAllTasksUnauthenticated() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/tasks"));

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(2)
    public void testGetAllTasksEmpty() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/tasks")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TaskDto> tasks = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class)
        );

        assertTrue(tasks.isEmpty());
    }

    @Test
    @Order(3)
    public void testGetAllTaskNotEmpty() throws Exception {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .user(user)
                .build();

        taskRepository.save(task);

        ResultActions response = mockMvc.perform(get("/api/tasks")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TaskDto> tasks = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class)
        );

        assertFalse(tasks.isEmpty());
        assertTrue(tasks.stream().anyMatch(t -> t.getId().equals(task.getId())));
        assertTrue(tasks.stream().anyMatch(t -> t.getName().equals(task.getName())));
        assertTrue(tasks.stream().anyMatch(t -> t.getDescription().equals(task.getDescription())));
        assertTrue(tasks.stream().anyMatch(t -> t.getStartDatetime().equals(task.getStartDatetime().truncatedTo(ChronoUnit.SECONDS))));
    }

    @Test
    @Order(4)
    public void testGetAllTasksOnlyAuthUser() throws Exception {

        User otherUser = User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();

        otherUser = userRepository.save(otherUser);

        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .user(user)
                .build();

        taskRepository.save(task);

        Task otherUserTask = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .user(otherUser)
                .build();

        taskRepository.save(otherUserTask);

        ResultActions response = mockMvc.perform(get("/api/tasks")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TaskDto> tasks = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class)
        );

        assertFalse(tasks.isEmpty());
        assertTrue(tasks.stream().anyMatch(t -> t.getId().equals(task.getId())));
        assertFalse(tasks.stream().anyMatch(t -> t.getId().equals(otherUserTask.getId())));
    }

    @Test
    @Order(5)
    public void testGetTaskByIdUnauthenticated() throws Exception {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .user(user)
                .build();

        task = taskRepository.save(task);

        ResultActions response = mockMvc.perform(get("/api/tasks/{id}", task.getId()));

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(6)
    public void testGetTaskByIdNotFound() throws Exception {
        long nonExistentTaskId = 999L;

        ResultActions response = mockMvc.perform(get("/api/tasks/{id}", nonExistentTaskId)
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(7)
    public void testGetTaskById() throws Exception {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .user(user)
                .build();

        task = taskRepository.save(task);

        ResultActions response = mockMvc.perform(get("/api/tasks/{id}", task.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        TaskDto taskDto = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                TaskDto.class
        );

        assertNotNull(taskDto);
        assertEquals(task.getId(), taskDto.getId());
        assertEquals(task.getName(), taskDto.getName());
        assertEquals(task.getDescription(), taskDto.getDescription());
        assertEquals(task.getStartDatetime().truncatedTo(ChronoUnit.SECONDS), taskDto.getStartDatetime());
    }

    @Test
    @Order(8)
    public void testGetTaskByIdForbidden() throws Exception {
        User otherUser = User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();

        otherUser = userRepository.save(otherUser);

        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .user(otherUser)
                .build();

        task = taskRepository.save(task);

        ResultActions response = mockMvc.perform(get("/api/tasks/{id}", task.getId())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(9)
    public void testGetCompletedTasksUnauthenticated() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/tasks/completed"));

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(10)
    public void testGetCompletedTasksEmpty() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/tasks/completed")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TaskDto> tasks = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class)
        );

        assertTrue(tasks.isEmpty());
    }

    @Test
    @Order(11)
    public void testGetCompletedTasksNotEmpty() throws Exception {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(true)
                .startDatetime(LocalDateTime.now())
                .user(user)
                .build();

        taskRepository.save(task);

        Task notCompletedTask = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .user(user)
                .build();

        taskRepository.save(notCompletedTask);

        ResultActions response = mockMvc.perform(get("/api/tasks/completed")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TaskDto> tasks = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class)
        );

        assertFalse(tasks.isEmpty());
        assertTrue(tasks.stream().anyMatch(t -> t.getId().equals(task.getId())));
        assertTrue(tasks.stream().anyMatch(t -> t.getName().equals(task.getName())));
        assertTrue(tasks.stream().anyMatch(t -> t.getDescription().equals(task.getDescription())));
        assertTrue(tasks.stream().anyMatch(TaskDto::isCompleted));
    }

    @Test
    @Order(12)
    public void testGetPendingTasksUnauthenticated() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/tasks/pending"));

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(13)
    public void testGetPendingTasksEmpty() throws Exception {

        taskRepository.deleteAll();

        ResultActions response = mockMvc.perform(get("/api/tasks/pending")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TaskDto> tasks = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class)
        );

        assertTrue(tasks.isEmpty());
    }

    @Test
    @Order(14)
    public void testGetPendingTasksNotEmpty() throws Exception {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .user(user)
                .build();

        taskRepository.save(task);

        Task completedTask = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(true)
                .startDatetime(LocalDateTime.now())
                .user(user)
                .build();

        taskRepository.save(completedTask);

        ResultActions response = mockMvc.perform(get("/api/tasks/pending")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TaskDto> tasks = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TaskDto.class)
        );

        assertFalse(tasks.isEmpty());
        assertTrue(tasks.stream().anyMatch(t -> t.getId().equals(task.getId())));
        assertTrue(tasks.stream().anyMatch(t -> t.getName().equals(task.getName())));
        assertTrue(tasks.stream().anyMatch(t -> t.getDescription().equals(task.getDescription())));
        assertFalse(tasks.stream().anyMatch(TaskDto::isCompleted));
    }

    @Test
    @Order(15)
    public void testCreateTaskUnauthenticated() throws Exception {
        CreateTaskRequest task = CreateTaskRequest.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .build();

        ResultActions response = mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(16)
    public void testCreateTaskInvalidRequest() throws Exception {
        CreateTaskRequest task = CreateTaskRequest.builder()
                .name("")
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now())
                .build();

        ResultActions response = mockMvc.perform(post("/api/tasks")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));

        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(17)
    public void testCreateTaskValidRequest() throws Exception {
        CreateTaskRequest task = CreateTaskRequest.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().sentence())
                .completed(false)
                .startDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .endDatetime(LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS))
                .build();

        ResultActions response = mockMvc.perform(post("/api/tasks")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)));

        response.andDo(print())
                .andExpect(status().isOk());

        TaskDto createdTask = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                TaskDto.class
        );

        assertNotNull(createdTask);
        assertNotNull(createdTask.getId());
        assertEquals(task.getName(), createdTask.getName());
        assertEquals(task.getDescription(), createdTask.getDescription());
        assertEquals(task.getStartDatetime().truncatedTo(ChronoUnit.SECONDS), createdTask.getStartDatetime());
    }

}
