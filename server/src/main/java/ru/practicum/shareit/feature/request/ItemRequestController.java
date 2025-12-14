package ru.practicum.shareit.feature.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feature.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDto;
import ru.practicum.shareit.feature.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.feature.request.service.ItemRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(HEADER_USER_ID) Long userId,
                                 @RequestBody CreateItemRequestDto request) {
        return itemRequestService.create(userId, request);
    }

    @GetMapping
    public List<ItemRequestDtoWithItems> getUserRequests(@RequestHeader(HEADER_USER_ID) Long userId) {
        return itemRequestService.findAllByRequesterId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests() {
        return itemRequestService.findAll();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItems getRequestById(@PathVariable Long requestId) {
        return itemRequestService.findById(requestId);
    }
}
