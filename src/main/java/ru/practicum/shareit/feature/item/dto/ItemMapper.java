package ru.practicum.shareit.feature.item.dto;

import ru.practicum.shareit.feature.booking.model.Booking;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.request.UpdateItemRequest;
import ru.practicum.shareit.feature.item.model.Comment;
import ru.practicum.shareit.feature.item.model.Item;

import java.util.List;

public class ItemMapper {
    public static ItemDto mapToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static ItemDetailsDto mapToDetailsDto(
            Item item, List<Comment> comments, Booking lastBooking, Booking nextBooking) {

        BookingDateDto lastBookingDate = null;
        if (lastBooking != null) {
            lastBookingDate = new BookingDateDto();
            lastBookingDate.setStart(lastBooking.getStartDate());
            lastBookingDate.setEnd(lastBooking.getEndDate());
        }


        BookingDateDto nextBookingDate = null;
        if (nextBooking != null) {
            nextBookingDate = new BookingDateDto();
            nextBookingDate.setStart(nextBooking.getStartDate());
            nextBookingDate.setEnd(nextBooking.getEndDate());
        }

        return ItemDetailsDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments.stream()
                    .map(CommentDto::fromModel)
                    .toList())
                .lastBooking(lastBookingDate)
                .nextBooking(nextBookingDate)
                .build();
    }

    public static Item mapToModel(CreateItemRequest request) {
        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable());
        return item;
    }

    public static Item updateModelFields(Item item, UpdateItemRequest request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }

        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }

        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }

        return item;
    }
}
