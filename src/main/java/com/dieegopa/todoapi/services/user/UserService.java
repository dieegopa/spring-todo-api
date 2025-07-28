package com.dieegopa.todoapi.services.user;

import com.dieegopa.todoapi.dtos.RegisterUserRequest;
import com.dieegopa.todoapi.dtos.UserDto;
import com.dieegopa.todoapi.exceptions.DuplicateUserException;
import com.dieegopa.todoapi.mappers.UserMapper;
import com.dieegopa.todoapi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException();
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return userMapper.toUserDto(user);
    }
}
