package com.dieegopa.todoapi.repositories;

import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT t FROM Tag t WHERE t.user = :user")
    List<Tag> getAllByUser(@Param("user") User user);

    @Query("SELECT t FROM Tag t WHERE t.id IN :ids AND t.user = :user")
    Set<Tag> findAllByIdInAndUser(Collection<Long> ids, User user);
}