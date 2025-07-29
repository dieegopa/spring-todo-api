package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerUnitTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoginOk() throws Exception {

        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());

    }

    @Test
    public void testLoginUnauthorized() throws Exception {

        LoginRequest loginRequest = new LoginRequest(user.getEmail(), "wrongPassword");

        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        response.andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testRefreshAccessTokenOk() throws Exception {

        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());

        String refreshToken = Objects.requireNonNull(response.andReturn().getResponse().getCookie("refreshToken")).getValue();

        ResultActions refreshResponse = mockMvc.perform(post("/api/auth/refresh")
                .cookie(new Cookie("refreshToken", refreshToken))
                .contentType(MediaType.APPLICATION_JSON)
        );

        refreshResponse.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    public void testRefreshAccessTokenUnauthorized() throws Exception {

        ResultActions response = mockMvc.perform(post("/api/auth/refresh")
                .cookie(new Cookie("refreshToken", "invalidToken"))
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
