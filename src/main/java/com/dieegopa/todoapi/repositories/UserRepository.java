package com.dieegopa.todoapi.repositories;

import com.dieegopa.todoapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}