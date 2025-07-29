package com.dieegopa.todoapi;

import com.dieegopa.todoapi.entities.User;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;

public class BaseTest {

    public Faker faker;

    public User user;

    @BeforeEach
    public void setUp() {

        faker = new Faker();

        user = User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();

    }

}
