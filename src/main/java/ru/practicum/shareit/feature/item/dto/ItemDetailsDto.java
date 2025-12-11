package ru.practicum.shareit.feature.item.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.List;

@Value
@ToString
@Builder
public class ItemDetailsDto {
    Long id;
    String name;
    String description;
    Boolean available;
    List<CommentDto> comments;
    BookingDateDto lastBooking;
    BookingDateDto nextBooking;
}
