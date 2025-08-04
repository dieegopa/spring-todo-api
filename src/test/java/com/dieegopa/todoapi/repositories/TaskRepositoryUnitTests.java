package com.dieegopa.todoapi.repositories;

import com.dieegopa.todoapi.BaseJpaTest;
import com.dieegopa.todoapi.entities.Task;
import com.dieegopa.todoapi.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TaskRepositoryUnitTests extends BaseJpaTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveTask() {
        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .build();

        taskRepository.save(task);

        assertNotNull(task.getId());
    }

    @Test
    public void testGetAllByUser() {
        User otherUser = User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();

        Task task = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(user)
                .build();

        taskRepository.save(task);

        Task otherTask = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(false)
                .user(otherUser)
                .build();

        userRepository.save(otherUser);
        taskRepository.save(otherTask);

        var tasks = taskRepository.getAllByUser(user);
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertFalse(tasks.stream().anyMatch(t -> t.getUser().getId().equals(otherUser.getId())));
    }

    @Test
    public void testGetAllByUserAndCompleted() {
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
                .completed(true)
                .user(user)
                .build();

        taskRepository.save(task);

        Task otherTask = Task.builder()
                .name(faker.lorem().word())
                .description(faker.lorem().paragraph())
                .startDatetime(LocalDateTime.now())
                .completed(true)
                .user(otherUser)
                .build();

        taskRepository.save(otherTask);

        var tasks = taskRepository.getAllByUserAndCompleted(user, true);
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertTrue(tasks.stream().anyMatch(Task::isCompleted));
        assertFalse(tasks.stream().anyMatch(t -> t.getUser().getId().equals(otherUser.getId())));
    }

}
