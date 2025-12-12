package ru.practicum.shareit.feature.item.dto.request;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@Builder
public class CreateItemRequest {
    String name;
    String description;
    Boolean available;
    Long requestId;
}
