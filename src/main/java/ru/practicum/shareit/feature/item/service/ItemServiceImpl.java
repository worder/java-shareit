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
import ru.practicum.shareit.feature.user.dal.UserRepository;
import ru.practicum.shareit.feature.user.service.UserService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;

    @Override
    public ItemDto create(CreateItemRequest request) {
        Item item = ItemMapper.mapToModel(request);

        item.setOwner(userStorage.findById(request.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Owner Not Found")));

        Item createdItem = itemStorage.save(item);
        log.info("Created item: {} from request: {}", createdItem, request);
        return ItemMapper.mapToDto(createdItem);
    }

    @Override
    public ItemDto read(Long id) {
        return itemStorage.findById(id)
                .map(ItemMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }

    @Override
    public ItemDto update(Long id, UpdateItemRequest request) {
        Item currentItem = itemStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        Item updatedItem = ItemMapper.updateModelFields(currentItem, request);

        if (!currentItem.getOwner().getId().equals(request.getOwner())) {
            throw new NotFoundException("Invalid owner id");
        }

        updatedItem = itemStorage.save(updatedItem);
        log.info("Updated item: {} with request: {}; new item: {}", currentItem, request, updatedItem);
        return ItemMapper.mapToDto(updatedItem);
    }

    @Override
    public void delete(Long id) {
        if (!itemStorage.existsById(id)) {
            throw new NotFoundException("Item not found");
        }

        log.info("Deleted item: {}", itemStorage.findById(id));
        itemStorage.deleteById(id);
    }

    @Override
    public List<ItemDto> getUserItems(Long id) {
        return itemStorage.findByOwnerId(id).stream()
                .map(ItemMapper::mapToDto)
                .toList();
    }

    @Override
    public List<ItemDto> findUserItems(Long id, String text) {
        if (!text.isBlank()) {
            return itemStorage.findByNameOrDescription(id, text).stream()
                    .map(ItemMapper::mapToDto)
                    .toList();
        }

        return List.of();
    }
}
