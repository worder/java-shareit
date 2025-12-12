package ru.practicum.shareit.feature.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.item.dal.ItemRepository;
import ru.practicum.shareit.feature.item.model.Item;
import ru.practicum.shareit.feature.request.dal.ItemRequestRepository;
import ru.practicum.shareit.feature.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.feature.request.dto.ItemRequestMapper;
import ru.practicum.shareit.feature.request.model.ItemRequest;
import ru.practicum.shareit.feature.user.dal.UserRepository;
import ru.practicum.shareit.feature.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestStorage;
    private final UserRepository userStorage;
    private final ItemRepository itemStorage;

    @Override
    public ItemRequestDto create(Long userId, CreateItemRequestDto request) {
        ItemRequest model = ItemRequestMapper.mapToModel(request);
        User requester = userStorage.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        model.setRequester(requester);
        model.setCreated(LocalDateTime.now());

        log.info("Trying to add item request: {} from incoming data: {}", model, request);
        model = itemRequestStorage.save(model);
        log.info("Added item request successfully: {}", model);
        return ItemRequestMapper.mapToDto(model);
    }

    @Override
    public ItemRequestDtoWithItems findById(Long requestId) {
        ItemRequest model = itemRequestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Item request not found"));

        List<Item> responseItems = itemStorage.findByRequestId(requestId);

        return ItemRequestMapper.mapToDtoWithItems(model, responseItems);
    }

    @Override
    public List<ItemRequestDto> findAll() {
        return itemRequestStorage.findAllByOrderByCreated().stream()
                .map(ItemRequestMapper::mapToDto)
                .toList();
    }

    @Override
    public List<ItemRequestDtoWithItems> findAllByRequesterId(Long requesterId) {
        return itemRequestStorage.findByRequesterIdOrderByCreated(requesterId).stream()
                .map(r -> ItemRequestMapper.mapToDtoWithItems(r, itemStorage.findByRequestId(r.getId())))
                .toList();
    }
}
