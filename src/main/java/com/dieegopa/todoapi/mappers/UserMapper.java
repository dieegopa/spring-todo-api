package com.dieegopa.todoapi.mappers;

import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.dtos.UserDto;
import com.dieegopa.todoapi.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);

    User toEntity(RegisterUserRequest registerUserRequest);
}
