package ru.practicum.shareit.feature.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.booking.model.Booking;
import ru.practicum.shareit.feature.booking.dal.BookingRepository;
import ru.practicum.shareit.feature.item.dto.request.CreateCommentRequest;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.request.UpdateItemRequest;
import ru.practicum.shareit.feature.item.model.Comment;
import ru.practicum.shareit.feature.item.model.Item;
import ru.practicum.shareit.feature.item.dal.CommentRepository;
import ru.practicum.shareit.feature.item.dal.ItemRepository;
import ru.practicum.shareit.feature.item.dto.*;
import ru.practicum.shareit.feature.request.dal.ItemRequestRepository;
import ru.practicum.shareit.feature.user.model.User;
import ru.practicum.shareit.feature.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemStorage;
    private final UserRepository userStorage;
    private final CommentRepository commentStorage;
    private final BookingRepository bookingStorage;
    private final ItemRequestRepository itemRequestStorage;

    @Override
    public ItemDto create(Long userId, CreateItemRequest request) {
        Item item = ItemMapper.mapToModel(request);

        item.setOwner(userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Owner Not Found")));

        if (request.getRequestId() != null) {
            itemRequestStorage.findById(request.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Request not found"));
        }

        Item createdItem = itemStorage.save(item);
        log.info("Created item: {} from request: {}", createdItem, request);
        return ItemMapper.mapToDto(createdItem);
    }

    @Override
    public ItemDetailsDto findById(Long userId, Long itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        if (item.getOwner().getId().equals(userId)) {
            return this.mapForOwnerDto(item);
        } else {
            return this.mapWithCommentsDto(item);
        }
    }

    @Override
    public ItemDto update(Long userId, Long itemId, UpdateItemRequest request) {
        Item currentItem = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        Item updatedItem = ItemMapper.updateModelFields(currentItem, request);

        if (!currentItem.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Invalid owner id");
        }

        updatedItem = itemStorage.save(updatedItem);
        log.info("Updated item: {} with request: {}; new item: {}", currentItem, request, updatedItem);
        return ItemMapper.mapToDto(updatedItem);
    }

    @Override
    public void delete(Long userId, Long id) {
        if (!itemStorage.existsById(id)) {
            throw new NotFoundException("Item not found");
        }

        log.info("Deleted item: {}", itemStorage.findById(id));
        itemStorage.deleteById(id);
    }

    @Override
    public List<ItemDetailsDto> getUserItems(Long userId) {
        return itemStorage.findByOwnerId(userId).stream()
                .map(this::mapForOwnerDto)
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

    @Override
    public CommentDto createComment(Long authorId, Long itemId, CreateCommentRequest request) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found"));

        User author = userStorage.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author not found"));

        bookingStorage.findPastBookingByItemIdAndBookerIdAndStatus(item.getId(), author.getId(), Booking.Status.APPROVED)
                .orElseThrow(() -> new BadRequestException("No suitable bookings found for item"));

        Comment comment = new Comment();
        comment.setText(request.text());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        comment = commentStorage.save(comment);
        log.info("Created comment: {}", comment);
        return CommentDto.fromModel(comment);
    }

    private ItemDetailsDto mapForOwnerDto(Item item) {
        return ItemMapper.mapToDetailsDto(
                item,
                commentStorage.findByItemId(item.getId()),
                bookingStorage.findLastBookingByItemId(item.getId(), Booking.Status.APPROVED).orElse(null),
                bookingStorage.findNextBookingForItemId(item.getId(), Booking.Status.APPROVED).orElse(null));
    }

    private ItemDetailsDto mapWithCommentsDto(Item item) {
        return ItemMapper.mapToDetailsDto(
                item,
                commentStorage.findByItemId(item.getId()),
                null,
                null);
    }
}
