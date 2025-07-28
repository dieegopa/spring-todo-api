package com.dieegopa.todoapi.repositories;

import com.dieegopa.todoapi.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}