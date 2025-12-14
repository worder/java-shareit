package ru.practicum.shareit.feature.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@AllArgsConstructor
@Value
@ToString
@Builder(toBuilder = true)
public class CreateItemRequest {
    String name;
    String description;
    Boolean available;
    Long requestId;
}
