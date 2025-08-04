package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.services.tag.ITagService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final ITagService tagService;

    @GetMapping
    public Iterable<TagDto> getAllTags() {
        return tagService.getAllTags();
    }

}
