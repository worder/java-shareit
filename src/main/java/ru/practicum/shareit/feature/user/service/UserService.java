package ru.practicum.shareit.feature.user.service;

import ru.practicum.shareit.feature.user.dto.request.CreateUserRequest;
import ru.practicum.shareit.feature.user.dto.request.UpdateUserRequest;
import ru.practicum.shareit.feature.user.dto.UserDto;

public interface UserService {
    UserDto create(CreateUserRequest request);

    UserDto findById(Long id);

    UserDto update(Long id, UpdateUserRequest request);

    void delete(Long id);

    boolean isUserExists(Long id);
}
