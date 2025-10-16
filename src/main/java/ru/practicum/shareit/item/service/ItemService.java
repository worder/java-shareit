package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.service.CrudService;

import java.util.List;

public interface ItemService extends CrudService<Long, ItemDto, CreateItemRequest, UpdateItemRequest> {
    public List<ItemDto> getUserItems(Long id);
    public List<ItemDto> findUserItems(Long id, String text);
}
