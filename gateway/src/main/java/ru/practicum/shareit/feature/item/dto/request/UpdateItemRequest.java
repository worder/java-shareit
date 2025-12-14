package ru.practicum.shareit.feature.item.dto.request;

import lombok.ToString;
import lombok.Value;

@Value
@ToString
public class UpdateItemRequest {
    String name;
    String description;
    Boolean available;
}
