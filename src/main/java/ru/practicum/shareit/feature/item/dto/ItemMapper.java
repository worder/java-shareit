package ru.practicum.shareit.feature.item.dto;

import ru.practicum.shareit.feature.item.Item;

public class ItemMapper {
    public static ItemDto mapToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item mapToModel(CreateItemRequest request) {
        return Item.builder()
                .owner(request.getOwner())
                .name(request.getName())
                .description(request.getDescription())
                .available(request.getAvailable())
                .build();
    }

    public static Item updateModelFields(Item item, UpdateItemRequest request) {
        Item.ItemBuilder builder = item.toBuilder();

        if (request.hasName()) {
            builder.name(request.getName());
        }

        if (request.hasDescription()) {
            builder.description(request.getDescription());
        }

        if (request.hasAvailable()) {
            builder.available(request.getAvailable());
        }

        if (request.hasOwner()) {
            builder.owner(request.getOwner());
        }

        return builder.build();
    }
}
