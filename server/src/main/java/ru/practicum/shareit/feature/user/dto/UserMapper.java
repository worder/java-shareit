package ru.practicum.shareit.feature.user.dto;

import ru.practicum.shareit.feature.user.dto.request.CreateUserRequest;
import ru.practicum.shareit.feature.user.dto.request.UpdateUserRequest;
import ru.practicum.shareit.feature.user.model.User;

public class UserMapper {
    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }

    public static User mapToModel(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        return user;
    }

    public static User updateModelFields(User user, UpdateUserRequest request) {
        if (request.hasName()) {
            user.setName(request.getName());
        }

        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }

        return user;
    }
}
