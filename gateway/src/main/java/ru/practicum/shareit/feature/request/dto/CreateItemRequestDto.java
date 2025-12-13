package ru.practicum.shareit.feature.request.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateItemRequestDto(@NotBlank String description) {
}
