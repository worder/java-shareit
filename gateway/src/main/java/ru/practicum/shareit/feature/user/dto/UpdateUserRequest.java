package ru.practicum.shareit.feature.user.dto;

import jakarta.validation.constraints.Email;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class UpdateUserRequest {
    String name;

    @Email
    String email;
}
