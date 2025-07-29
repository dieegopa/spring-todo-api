package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.exceptions.DuplicateUserException;
import com.dieegopa.todoapi.services.user.IUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceUnitTests extends BaseTest {

    @Autowired
    private IUserService userService;

    @Test
    public void testRegisterUser() {

        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .name(user.getName())
                .email(faker.internet().emailAddress())
                .password(user.getPassword())
                .build();

        var userDto = userService.registerUser(registerUserRequest);

        Assertions.assertNotNull(userDto);
    }

    @Test
    public void testRegisterUserWithDuplicateEmail() {
        RegisterUserRequest registerUserRequest = RegisterUserRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        Assertions.assertThrows(DuplicateUserException.class, () -> userService.registerUser(registerUserRequest));
    }
}
