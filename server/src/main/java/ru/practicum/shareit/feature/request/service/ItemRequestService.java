package ru.practicum.shareit.feature.request.service;

import ru.practicum.shareit.feature.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDtoWithItems;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, CreateItemRequestDto request);

    ItemRequestDtoWithItems findById(Long requestId);

    List<ItemRequestDtoWithItems> findAllByRequesterId(Long userId);

    List<ItemRequestDto> findAll();
}
