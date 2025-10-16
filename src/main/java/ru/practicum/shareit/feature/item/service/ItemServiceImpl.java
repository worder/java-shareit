package ru.practicum.shareit.feature.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.item.Item;
import ru.practicum.shareit.feature.item.dal.ItemRepository;
import ru.practicum.shareit.feature.item.dto.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.ItemDto;
import ru.practicum.shareit.feature.item.dto.ItemMapper;
import ru.practicum.shareit.feature.item.dto.UpdateItemRequest;
import ru.practicum.shareit.feature.user.service.UserService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository storage;
    private final UserService userService;

    @Override
    public ItemDto create(CreateItemRequest request) {
        Item item = ItemMapper.mapToModel(request);
        if (!userService.isUserExists(item.getOwner())) {
            throw new NotFoundException("Owner not found");
        }

        Item createdItem = storage.create(item);
        log.info("Created item: {} from request: {}", createdItem, request);
        return ItemMapper.mapToDto(createdItem);
    }

    @Override
    public ItemDto read(Long id) {
        return storage.findById(id)
                .map(ItemMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public ItemDto update(Long id, UpdateItemRequest request) {
        Item currentItem = storage.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        Item updatedItem = ItemMapper.updateModelFields(currentItem, request);
        if (!userService.isUserExists(updatedItem.getOwner())) {
            throw new NotFoundException("Owner not found");
        }

        updatedItem = storage.update(updatedItem);
        log.info("Updated item: {} with request: {}; new item: {}", currentItem, request, updatedItem);
        return ItemMapper.mapToDto(updatedItem);
    }

    @Override
    public void delete(Long id) {
        Item currentItem = storage.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
        log.info("Deleted item: {}", currentItem);
        storage.delete(id);
    }

    @Override
    public List<ItemDto> getUserItems(Long id) {
        return storage.findByOwnerId(id).stream()
                .map(ItemMapper::mapToDto)
                .toList();
    }

    @Override
    public List<ItemDto> findUserItems(Long id, String text) {
        return storage.findByNameOrDescription(id, text).stream()
                .map(ItemMapper::mapToDto)
                .toList();
    }
}
