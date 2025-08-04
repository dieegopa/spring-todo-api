package com.dieegopa.todoapi.services.tag;

import com.dieegopa.todoapi.dtos.TagDto;

public interface ITagService {
    Iterable<TagDto> getAllTags();
}
