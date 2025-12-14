package ru.practicum.shareit.feature.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@AllArgsConstructor
@Value
@Builder
public class ItemRequestDto {
    Long id;
    String description;
    LocalDateTime created;
}
