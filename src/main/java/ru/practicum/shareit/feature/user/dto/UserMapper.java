package ru.practicum.shareit.feature.user.dto;

import ru.practicum.shareit.feature.user.User;

public class UserMapper {
    public static UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }

    public static User mapToModel(CreateUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail()).build();
    }

    public static User updateModelFields(User user, UpdateUserRequest request) {
        User.UserBuilder builder = user.toBuilder();

        if (request.hasName()) {
            builder.name(request.getName());
        }
        if (request.hasEmail()) {
            builder.email(request.getEmail());
        }

        return builder.build();
    }
}
