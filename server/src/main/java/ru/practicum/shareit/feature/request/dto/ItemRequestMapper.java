package ru.practicum.shareit.feature.request.dto;

import ru.practicum.shareit.feature.item.model.Item;
import ru.practicum.shareit.feature.request.model.ItemRequest;

import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestDto mapToDto(ItemRequest model) {
        return ItemRequestDto.builder()
                .id(model.getId())
                .description(model.getDescription())
                .created(model.getCreated())
                .build();
    }

    public static ItemRequestDtoWithItems mapToDtoWithItems(ItemRequest model, List<Item> responseItems) {
        return ItemRequestDtoWithItems.builder()
                .id(model.getId())
                .description(model.getDescription())
                .created(model.getCreated())
                .items(responseItems
                        .stream()
                        .map(i -> ItemRequestDtoWithItems.ResponseItemDao.builder()
                                .itemId(i.getId())
                                .name(i.getName())
                                .ownerId(i.getOwner().getId())
                                .build()
                        )
                        .toList()
                )
                .build();
    }

    public static ItemRequest mapToModel(CreateItemRequestDto createReq) {
        ItemRequest model = new ItemRequest();
        model.setDescription(createReq.description());

        return model;
    }
}
