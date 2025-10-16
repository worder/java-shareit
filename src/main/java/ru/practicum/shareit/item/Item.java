package ru.practicum.shareit.item;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    Long request;
}
