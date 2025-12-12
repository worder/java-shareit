package ru.practicum.shareit.feature.item.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@Builder
public class CreateItemRequest {
    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;

    Long requestId;
}
