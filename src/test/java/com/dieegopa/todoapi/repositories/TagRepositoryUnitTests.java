package com.dieegopa.todoapi.repositories;

import com.dieegopa.todoapi.BaseJpaTest;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TagRepositoryUnitTests extends BaseJpaTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveTag() {
        var tag = new Tag();
        tag.setName(faker.lorem().word());
        tag.setUser(user);

        tagRepository.save(tag);

        assertNotNull(tag.getId());
        assertNotNull(tag.getUser());
    }

    @Test
    public void testGetAllByUser() {
        User otherUser = User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();

        userRepository.save(otherUser);

        var tag = new Tag();
        tag.setName(faker.lorem().word());
        tag.setUser(user);

        tagRepository.save(tag);

        var otherTag = new Tag();
        otherTag.setName(faker.lorem().word());
        otherTag.setUser(otherUser);
        tagRepository.save(otherTag);

        var tags = tagRepository.getAllByUser(user);

        assertNotNull(tags);
        assertFalse(tags.isEmpty());
        assertTrue(tags.stream().anyMatch(t -> t.getName().equals(tag.getName())));
        assertFalse(tags.stream().anyMatch(t -> t.getUser().getId().equals(otherUser.getId())));
    }
}
