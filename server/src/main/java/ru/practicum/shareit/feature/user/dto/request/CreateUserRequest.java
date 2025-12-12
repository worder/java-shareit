package ru.practicum.shareit.feature.user.dto.request;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class CreateUserRequest {
    String name;
    String email;
}
