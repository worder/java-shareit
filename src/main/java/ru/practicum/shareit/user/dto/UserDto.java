package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@Builder
public class UserDto {
    Long id;
    String name;
    String email;
}
