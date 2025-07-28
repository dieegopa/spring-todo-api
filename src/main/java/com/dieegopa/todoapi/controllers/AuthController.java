package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.config.JwtConfig;
import com.dieegopa.todoapi.dtos.JwtResponse;
import com.dieegopa.todoapi.dtos.LoginRequest;
import com.dieegopa.todoapi.services.auth.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication operations")
public class AuthController {

    private final IAuthService authService;
    private final JwtConfig jwtConfig;

    @SecurityRequirements
    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user and returns a JWT access token."
    )
    public JwtResponse login(
            @Parameter(
                    description = "Login credentials for the user",
                    required = true
            )
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        var loginResult = authService.login(loginRequest);
        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResult.getAccessToken().toString());

    }

    @SecurityRequirements
    @PostMapping("/refresh")
    @Operation(
            summary = "Refresh access token",
            description = "Refreshes the JWT access token using a valid refresh token stored in a cookie."
    )
    public JwtResponse refresh(
            @Parameter(
                    description = "Refresh token stored in a cookie",
                    required = true
            )
            @CookieValue(value = "refreshToken") String refreshToken
    ) {
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }
}
