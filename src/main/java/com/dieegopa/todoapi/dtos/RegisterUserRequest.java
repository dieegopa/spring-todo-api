package com.dieegopa.todoapi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {

    @NotNull
    @NotBlank(message = "Name should not be blank")
    private String name;

    @NotNull
    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotNull
    @NotBlank(message = "Password should not be blank")
    private String password;
}
