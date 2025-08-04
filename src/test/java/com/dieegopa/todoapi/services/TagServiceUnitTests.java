package com.dieegopa.todoapi.services;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.repositories.TagRepository;
import com.dieegopa.todoapi.services.tag.ITagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
