package com.dieegopa.todoapi.services.auth;

import com.dieegopa.todoapi.entities.Jwt;
import com.dieegopa.todoapi.entities.User;

public interface IJwtService {
    Jwt generateAccessToken(User user);

    Jwt generateRefreshToken(User user);

    Jwt parseToken(String token);
}
