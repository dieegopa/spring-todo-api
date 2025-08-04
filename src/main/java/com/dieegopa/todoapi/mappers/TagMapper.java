package com.dieegopa.todoapi.mappers;

import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.entities.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto toDto(Tag tag);
}
