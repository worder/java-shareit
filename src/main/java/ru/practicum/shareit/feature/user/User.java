package ru.practicum.shareit.feature.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {
    Long id;
    String name;
    String email;
}
