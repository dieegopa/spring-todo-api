package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerUnitTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    public void testRegisterUser() throws Exception {

        String email = faker.internet().emailAddress();

        var registerUserRequest = RegisterUserRequest.builder()
                .name(user.getName())
                .email(email)
                .password(user.getPassword())
                .build();

        ResultActions response = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserRequest))
        );

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is(user.getName()))
                ).andExpect(jsonPath("$.email",
                        is(email))
                ).andExpect(jsonPath("$.id",
                        notNullValue()
                ));

        user = userRepository.findByEmail(user.getEmail()).orElse(null);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(2, userRepository.count());
    }

    @Test
    public void testRegisterUserWithExistingEmail() throws Exception {
        var registerUserRequest = RegisterUserRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        ResultActions response = mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerUserRequest))
        );

        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("User already exists with the provided email.")));
    }
}
