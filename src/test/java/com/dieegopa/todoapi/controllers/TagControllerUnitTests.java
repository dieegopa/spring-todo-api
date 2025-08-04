package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.BaseTest;
import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.repositories.TagRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TagControllerUnitTests extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @Order(1)
    public void testGetAllTagsUnauthenticated() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/tags"));

        resultActions.andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(2)
    public void testGetAllTagsEmpty() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/tags")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TagDto> tags = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TagDto.class)
        );

        assertTrue(tags.isEmpty());

    }

    @Test
    @Order(3)
    public void testGetAllTagsNotEmpty() throws Exception {
        Tag tag = new Tag();
        tag.setName(faker.lorem().word());
        tag.setUser(user);

        tagRepository.save(tag);

        ResultActions response = mockMvc.perform(get("/api/tags")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
        );

        response.andDo(print())
                .andExpect(status().isOk());

        List<TagDto> tags = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, TagDto.class)
        );

        assertFalse(tags.isEmpty());
        assertTrue(tags.stream().anyMatch(t -> t.getName().equals(tag.getName())));
    }
}