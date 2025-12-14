package ru.practicum.shareit.feature.item.service;

import ru.practicum.shareit.feature.item.dto.*;
import ru.practicum.shareit.feature.item.dto.request.CreateCommentRequest;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.request.UpdateItemRequest;

import java.util.List;

public interface ItemService {

    ItemDto create(Long userId, CreateItemRequest item);

    ItemDetailsDto findById(Long userId, Long itemId);

    ItemDto update(Long userId, Long itemId, UpdateItemRequest request);

    void delete(Long userId, Long itemId);

    List<ItemDetailsDto> getUserItems(Long id);

    List<ItemDto> findUserItems(Long id, String text);

    CommentDto createComment(Long userId, Long itemId, CreateCommentRequest request);
}
