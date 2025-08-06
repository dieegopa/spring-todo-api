package com.dieegopa.todoapi.services.tag;

import com.dieegopa.todoapi.dtos.TagDto;
import com.dieegopa.todoapi.entities.Tag;
import com.dieegopa.todoapi.entities.User;
import com.dieegopa.todoapi.exceptions.UnprocessableEntityException;
import com.dieegopa.todoapi.mappers.TagMapper;
import com.dieegopa.todoapi.repositories.TagRepository;
import com.dieegopa.todoapi.services.auth.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    @Override
    public Set<Tag> validateAndGetTagsForUser(Set<Long> tagIds, User user) {
        Set<Tag> userTags = tagRepository.findAllByIdInAndUser(tagIds, user);

        if (userTags.size() != tagIds.size()) {
            throw new UnprocessableEntityException("One or more tags are invalid or do not belong to the current user");
        }

        return userTags;
    }

}
