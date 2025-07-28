package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.dtos.UserDto;
import com.dieegopa.todoapi.services.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management operations")
public class UserController {

    private final IUserService userService;

    @SecurityRequirements
    @PostMapping("/register")
    @Operation(summary = "Register a new user",
            description = "Creates a new user account with the provided details."
    )
    public UserDto registerUser(
            @Parameter(
                    description = "Details of the user to register",
                    required = true
            )
            @Valid @RequestBody RegisterUserRequest request
    ) {
        return userService.registerUser(request);
    }
}
