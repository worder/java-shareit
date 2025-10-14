package ru.practicum.shareit.user.service;

import ru.practicum.shareit.service.CrudService;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService extends CrudService<Long, UserDto, CreateUserRequest, UpdateUserRequest> {
}
