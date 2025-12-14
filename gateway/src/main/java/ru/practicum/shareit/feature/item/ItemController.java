package ru.practicum.shareit.feature.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feature.item.dto.request.CreateCommentRequest;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.request.UpdateItemRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemClient itemClient;

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader(HEADER_USER_ID) Long userId) {
        return itemClient.getUserItems(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @RequestBody @Valid CreateItemRequest request) {
        return itemClient.create(userId, request);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @PathVariable Long itemId,
                                         @RequestBody @Valid UpdateItemRequest request) {
        return itemClient.update(userId, itemId, request);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@RequestHeader(HEADER_USER_ID) Long userId,
                                      @PathVariable Long itemId) {
        return itemClient.findById(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(HEADER_USER_ID) Long userId,
                                         @RequestParam String text) {
        return itemClient.findUserItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(HEADER_USER_ID) Long authorId,
                                    @PathVariable Long itemId,
                                    @RequestBody @Valid CreateCommentRequest request) {
        return itemClient.createComment(authorId, itemId, request);
    }
}
