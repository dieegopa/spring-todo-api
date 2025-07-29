package com.dieegopa.todoapi.controller;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.entities.User;
import com.dieegopa.todoapi.repositories.UserRepository;
import com.dieegopa.todoapi.services.auth.IJwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerUnitTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private IJwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }

    @Test
    public void registerUserTest() throws Exception {

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",
                        is(user.getName()))
                ).andExpect(jsonPath("$.email",
                        is(user.getEmail()))
                ).andExpect(jsonPath("$.id",
                        is(1)
                ));

        user = userRepository.findByEmail(user.getEmail()).orElse(null);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, userRepository.count());

    }
}
