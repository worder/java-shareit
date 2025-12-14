package ru.practicum.shareit.feature.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class CreateUserRequest {
    @NotBlank
    String name;

    @NotNull
    @Email
    String email;
}
