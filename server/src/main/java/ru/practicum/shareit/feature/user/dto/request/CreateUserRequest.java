package ru.practicum.shareit.feature.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@AllArgsConstructor
@Value
@ToString
@Builder
public class CreateUserRequest {
    String name;
    String email;
}
