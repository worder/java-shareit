package ru.practicum.shareit.feature.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feature.item.dto.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.ItemDto;
import ru.practicum.shareit.feature.item.dto.UpdateItemRequest;
import ru.practicum.shareit.feature.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private static final String HEADER_OWNER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader(HEADER_OWNER_ID) Long ownerId) {
        return itemService.getUserItems(ownerId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(HEADER_OWNER_ID) Long ownerId,
                          @RequestBody @Valid CreateItemRequest request) {
        return itemService.create(request.toBuilder().ownerId(ownerId).build());
    }

    @PatchMapping("/{id}")
    public ItemDto update(@RequestHeader(HEADER_OWNER_ID) Long ownerId,
                          @PathVariable Long id,
                          @RequestBody @Valid UpdateItemRequest request) {
        return itemService.update(id, request.toBuilder().owner(ownerId).build());
    }

    @GetMapping("/{id}")
    public ItemDto get(@PathVariable Long id) {
        return itemService.read(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(HEADER_OWNER_ID) Long ownerId,
                                @RequestParam String text) {
        return itemService.findUserItems(ownerId, text);
    }
}
