package ru.practicum.shareit.feature.item.service;

import ru.practicum.shareit.feature.item.dto.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.ItemDto;
import ru.practicum.shareit.feature.item.dto.UpdateItemRequest;
import ru.practicum.shareit.common.service.CrudService;

import java.util.List;

public interface ItemService extends CrudService<Long, ItemDto, CreateItemRequest, UpdateItemRequest> {
    public List<ItemDto> getUserItems(Long id);

    public List<ItemDto> findUserItems(Long id, String text);
}
