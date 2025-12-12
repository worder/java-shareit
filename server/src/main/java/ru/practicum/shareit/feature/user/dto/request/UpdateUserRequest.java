package ru.practicum.shareit.feature.user.dto.request;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class UpdateUserRequest {
    String name;
    String email;

    public boolean hasName() {
        return name != null && !name.isBlank();
    }

    public boolean hasEmail() {
        return email != null && !email.isBlank();
    }
}
