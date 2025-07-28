package com.dieegopa.todoapi.services.auth;

import com.dieegopa.todoapi.dtos.Jwt;
import com.dieegopa.todoapi.dtos.LoginRequest;
import com.dieegopa.todoapi.dtos.LoginResponse;

public interface IAuthService {

    LoginResponse login(LoginRequest request);

    Jwt refreshAccessToken(String refreshToken);
}
