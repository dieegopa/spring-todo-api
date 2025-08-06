package com.dieegopa.todoapi.services.tag;

import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.entities.User;

import java.util.Set;

public interface ITagService {
    Iterable<TagDto> getAllTags();

    Set<Tag> validateAndGetTagsForUser(Set<Long> tagIds, User user);
}
