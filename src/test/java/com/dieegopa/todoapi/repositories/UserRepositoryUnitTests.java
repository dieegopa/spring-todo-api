package com.dieegopa.todoapi.repositories;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.entities.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryUnitTests extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Test UserRepository save method")
    public void testSaveUser() {
        userRepository.save(user);

        Assertions.assertNotNull(user.getId());
    }

    @Test
    @DisplayName("Test UserRepository findByEmail method")
    public void testFindByEmail() {

        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();

        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);
        User foundUser = userRepository.findByEmail(email).orElse(null);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(name, foundUser.getName());
        Assertions.assertEquals(email, foundUser.getEmail());
    }

    @Test
    @DisplayName("Test UserRepository existsByEmail method")
    public void testExistsByEmail() {
        String email = faker.internet().emailAddress();

        user.setEmail(email);

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail(email);
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Test UserRepository findById method")
    public void testFindById() {
        userRepository.save(user);
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    @DisplayName("Test UserRepository deleteById method")
    public void testDeleteById() {
        userRepository.save(user);
        Long userId = user.getId();

        userRepository.deleteById(userId);
        User deletedUser = userRepository.findById(userId).orElse(null);

        Assertions.assertNull(deletedUser);
    }

    @Test
    @DisplayName("Test UserRepository count method")
    public void testCount() {
        long count = userRepository.count();

        Assertions.assertEquals(1, count);
    }

    @Test
    @DisplayName("Test UserRepository findAll method")
    public void testFindAll() {
        userRepository.save(user);
        var users = userRepository.findAll();

        Assertions.assertFalse(users.isEmpty());
        Assertions.assertTrue(users.stream().anyMatch(u -> u.getId().equals(user.getId())));
    }

    @Test
    @DisplayName("Test UserRepository update method")
    public void testUpdateUser() {
        userRepository.save(user);
        String updatedName = faker.name().fullName();
        user.setName(updatedName);

        userRepository.save(user);
        User updatedUser = userRepository.findById(user.getId()).orElse(null);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(updatedName, updatedUser.getName());
    }

}
