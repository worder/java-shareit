package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Value;

@Value
public class UpdateUserRequest {
    String name;

    @Email
    String email;

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }
}
