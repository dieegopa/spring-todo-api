package com.dieegopa.todoapi.repositories;

import com.dieegopa.todoapi.BaseJpaTest;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;

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

    @Test
    public void testFindAllByIdInAndUser() {
        tagRepository.deleteAll();

        var tag1 = new Tag();
        tag1.setName(faker.lorem().word());
        tag1.setUser(user);
        tagRepository.save(tag1);

        var tag2 = new Tag();
        tag2.setName(faker.lorem().word());
        tag2.setUser(user);
        tagRepository.save(tag2);

        var otherUser = User.builder()
                .name(faker.name().fullName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();

        userRepository.save(otherUser);

        var otherTag = new Tag();
        otherTag.setName(faker.lorem().word());
        otherTag.setUser(otherUser);
        tagRepository.save(otherTag);

        var foundTags = tagRepository.findAllByIdInAndUser(Set.of(tag1.getId(), tag2.getId()), user);

        assertNotNull(foundTags);
        assertEquals(2, foundTags.size());
        assertTrue(foundTags.stream().anyMatch(t -> t.getName().equals(tag1.getName())));
        assertTrue(foundTags.stream().anyMatch(t -> t.getName().equals(tag2.getName())));
    }
}
