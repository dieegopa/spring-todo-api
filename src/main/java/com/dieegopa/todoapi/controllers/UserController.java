package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.dtos.UserDto;
import com.dieegopa.todoapi.services.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public UserDto registerUser(
            @RequestBody RegisterUserRequest request
    ) {
        return userService.registerUser(request);
    }
}
