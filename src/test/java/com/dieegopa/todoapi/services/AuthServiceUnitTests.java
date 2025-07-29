package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.entities.User;
import com.dieegopa.todoapi.services.auth.IAuthService;
import com.dieegopa.todoapi.services.auth.IJwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceUnitTests extends BaseTest {

    @Autowired
    private IJwtService jwtService;

    @Autowired
    private IAuthService authService;

    @Test
    public void testLogin() {

        var loginResponse = authService.login(loginRequest);
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getAccessToken());
        assertNotNull(loginResponse.getRefreshToken());
    }

    @Test
    public void testGetCurrentUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user.getId());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

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
