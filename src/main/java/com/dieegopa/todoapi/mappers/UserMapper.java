package com.dieegopa.todoapi.mappers;

import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.dtos.UserDto;
import com.dieegopa.todoapi.entities.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.LinkedHashSet;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);

    @AfterMapping
    default void initTags(@MappingTarget User user) {
        user.setTags(new LinkedHashSet<>());
    }
}
