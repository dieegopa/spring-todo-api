package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.entities.User;
import com.dieegopa.todoapi.services.auth.IAuthService;
import com.dieegopa.todoapi.services.auth.IJwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AuthServiceUnitTests extends BaseTest {

    @Autowired
    private IJwtService jwtService;

    @Autowired
    private IAuthService authService;

    @BeforeEach
    public void setup() {
        mockSecurityContext();
    }

    @Test
    public void testLogin() {

        var loginResponse = authService.login(loginRequest);
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getAccessToken());
        assertNotNull(loginResponse.getRefreshToken());
    }

    @Test
    public void testGetCurrentUser() {

        User result = authService.getCurrentUser();

        assertNotNull(result);
    }

    @Test
    public void testRefreshAccessToken() {
        var refreshToken = jwtService.generateRefreshToken(user);
        var newAccessToken = authService.refreshAccessToken(refreshToken.toString());

        assertNotNull(newAccessToken);
    }
}
