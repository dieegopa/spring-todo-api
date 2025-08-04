package com.dieegopa.todoapi.controllers;

import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.services.tag.ITagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tags")
@Tag(name = "Tags", description = "Operations related to tags")
public class TagController {

    private final ITagService tagService;

    @GetMapping
    @Operation(summary = "Get all tags", description = "Retrieves a list of all available tags for the user")
    @ApiResponse(
            responseCode = "200",
            description = "List of tags retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TagDto.class))
            )
    )
    public Iterable<TagDto> getAllTags() {
        return tagService.getAllTags();
    }

}
