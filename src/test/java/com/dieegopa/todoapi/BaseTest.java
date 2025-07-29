package com.dieegopa.todoapi;

import com.dieegopa.todoapi.dtos.LoginRequest;
import com.dieegopa.todoapi.entities.User;
import com.dieegopa.todoapi.repositories.UserRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class BaseTest {

    public Faker faker;

    public User user;

    @Autowired
    private UserRepository userRepository;

    public LoginRequest loginRequest;

    @BeforeEach
    public void setUp() {

        faker = new Faker();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (!userRepository.existsByEmail("first@gmail.com")) {
            user = User.builder()
                    .name(faker.name().fullName())
                    .email("first@gmail.com")
                    .password(passwordEncoder.encode("password1234"))
                    .build();
            userRepository.save(user);
        } else {
            user = userRepository.findByEmail("first@gmail.com").orElse(null);
        }

        loginRequest = new LoginRequest("first@gmail.com", "password1234");

    }

}
