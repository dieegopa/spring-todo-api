package com.dieegopa.todoapi.repositories;

import com.dieegopa.todoapi.entities.Task;
import com.dieegopa.todoapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.user = :user")
    List<Task> getAllByUser(@Param("user") User user);

    @Query("SELECT t FROM Task t WHERE t.user = :user AND t.completed = :completed")
    List<Task> getAllByUserAndCompleted(User user, boolean completed);
}