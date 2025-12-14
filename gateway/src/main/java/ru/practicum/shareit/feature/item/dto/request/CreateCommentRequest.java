package ru.practicum.shareit.feature.item.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(@NotBlank String text) {
}
