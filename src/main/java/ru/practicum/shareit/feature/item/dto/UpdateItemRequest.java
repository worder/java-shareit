package ru.practicum.shareit.feature.item.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@ToString
@Builder(toBuilder = true)
public class UpdateItemRequest {
    Long owner;

    String name;

    String description;

    Boolean available;


    public boolean hasName() {
        return this.name != null && !name.isBlank();
    }

    public boolean hasDescription() {
        return this.description != null && !description.isBlank();
    }

    public boolean hasAvailable() {
        return this.available != null;
    }
}
