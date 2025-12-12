package ru.practicum.shareit.feature.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feature.item.dto.*;
import ru.practicum.shareit.feature.item.dto.request.CreateCommentRequest;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.request.UpdateItemRequest;
import ru.practicum.shareit.feature.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public List<ItemDetailsDto> getUserItems(@RequestHeader(HEADER_USER_ID) Long userId) {
        return itemService.getUserItems(userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(HEADER_USER_ID) Long userId,
                          @RequestBody CreateItemRequest request) {
        return itemService.create(userId, request);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(HEADER_USER_ID) Long userId,
                          @PathVariable Long itemId,
                          @RequestBody UpdateItemRequest request) {
        return itemService.update(userId, itemId, request);
    }

    @GetMapping("/{itemId}")
    public ItemDetailsDto get(@RequestHeader(HEADER_USER_ID) Long userId,
                              @PathVariable Long itemId) {
        return itemService.findById(userId, itemId);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(HEADER_USER_ID) Long userId,
                       @PathVariable Long itemId) {
        itemService.delete(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestHeader(HEADER_USER_ID) Long userId,
                                @RequestParam String text) {
        return itemService.findUserItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader(HEADER_USER_ID) Long authorId,
                                    @PathVariable Long itemId,
                                    @RequestBody CreateCommentRequest request) {
        return itemService.createComment(authorId, itemId, request);
    }
}
