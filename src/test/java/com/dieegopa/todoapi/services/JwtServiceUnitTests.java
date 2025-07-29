package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.services.auth.IJwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class JwtServiceUnitTests extends BaseTest {

    @Autowired
    private IJwtService jwtService;

    @Test
    public void testGenerateAccessToken() {
        var accessToken = jwtService.generateAccessToken(user);

        assertNotNull(accessToken);
        assertEquals(accessToken.getClaims().getSubject(), user.getId().toString());
    }

    @Test
    public void testGenerateRefreshToken() {
        var refreshToken = jwtService.generateRefreshToken(user);

        assertNotNull(refreshToken);
        assertEquals(refreshToken.getClaims().getSubject(), user.getId().toString());
    }

    @Test
    public void testParseToken() {
        var accessToken = jwtService.generateAccessToken(user);
        var parsedToken = jwtService.parseToken(accessToken.toString());

        assertNotNull(parsedToken);
        assertNotNull(parsedToken.getClaims());
        assertNotNull(parsedToken.getClaims().getSubject());
        assertNotNull(parsedToken.getClaims().get("email"));
        assertNotNull(parsedToken.getClaims().get("name"));
        assertEquals(parsedToken.getClaims().getSubject(), user.getId().toString());
        assertEquals(parsedToken.getClaims().get("email"), user.getEmail());
        assertEquals(parsedToken.getClaims().get("name"), user.getName());
    }
}
