package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.dtos.UserDto;

public interface IUserService {

    UserDto registerUser(RegisterUserRequest registerUserRequest);
}
