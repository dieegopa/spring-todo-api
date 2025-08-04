package com.dieegopa.todoapi.services.tag;

import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.mappers.TagMapper;
import com.dieegopa.todoapi.repositories.TagRepository;
import com.dieegopa.todoapi.services.auth.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TagServiceImpl implements ITagService {

    private final TagRepository tagRepository;
    private final IAuthService authService;
    private final TagMapper tagMapper;

    @Override
    public Iterable<TagDto> getAllTags() {
        var user = authService.getCurrentUser();

        return tagRepository.getAllByUser(user)
                .stream()
                .map(tagMapper::toDto)
                .toList();
    }
}
