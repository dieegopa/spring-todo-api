package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.exceptions.UnprocessableEntityException;
import com.dieegopa.todoapi.repositories.TagRepository;
import com.dieegopa.todoapi.services.tag.ITagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TagServiceUnitTests extends BaseTest {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private ITagService tagService;

    @BeforeEach
    public void setup() {
        mockSecurityContext();
    }

    @Test
    public void testGetAllTags() {
        Tag tag = new Tag();
        tag.setName(faker.lorem().word());
        tag.setUser(user);

        tagRepository.save(tag);

        var tags = (List<TagDto>) tagService.getAllTags();
        assertNotNull(tags);
        assertFalse(tags.isEmpty());
    }

    @Test
    public void testValidateAndGetTagsForUser() {
        Tag tag1 = new Tag();
        tag1.setName(faker.lorem().word());
        tag1.setUser(user);
        tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName(faker.lorem().word());
        tag2.setUser(user);
        tagRepository.save(tag2);

        var tags = tagService.validateAndGetTagsForUser(Set.of(tag1.getId(), tag2.getId()), user);
        assertNotNull(tags);
        assertFalse(tags.isEmpty());
    }

    @Test
    public void testValidateAndGetTagsForUserThrowsException() {
        Tag tag1 = new Tag();
        tag1.setName(faker.lorem().word());
        tag1.setUser(user);
        tagRepository.save(tag1);

        Tag tag2 = new Tag();
        tag2.setName(faker.lorem().word());
        tag2.setUser(user);
        tagRepository.save(tag2);

        assertThrows(UnprocessableEntityException.class, () -> {
            tagService.validateAndGetTagsForUser(Set.of(tag1.getId(), 999L), user);
        });
    }
}
