package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@Builder
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available;
}
