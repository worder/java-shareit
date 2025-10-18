package ru.practicum.shareit.feature.user.service;

import ru.practicum.shareit.common.service.CrudService;
import ru.practicum.shareit.feature.user.dto.CreateUserRequest;
import ru.practicum.shareit.feature.user.dto.UpdateUserRequest;
import ru.practicum.shareit.feature.user.dto.UserDto;

public interface UserService extends CrudService<Long, UserDto, CreateUserRequest, UpdateUserRequest> {
    boolean isUserExists(Long id);
}
