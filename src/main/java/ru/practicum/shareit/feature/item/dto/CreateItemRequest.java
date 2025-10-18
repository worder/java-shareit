package ru.practicum.shareit.feature.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@Builder(toBuilder = true)
public class CreateItemRequest {
    Long owner;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;
}
