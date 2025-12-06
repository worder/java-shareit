package ru.practicum.shareit.feature.booking.dto;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import ru.practicum.shareit.feature.booking.Booking;
import ru.practicum.shareit.feature.item.dto.ItemDto;
import ru.practicum.shareit.feature.user.dto.UserDto;

import java.time.LocalDateTime;

@Value
@ToString
@Builder
public class BookingDto {
    Long id;
    ItemDto item;
    UserDto booker;
    LocalDateTime start;
    LocalDateTime end;
    Booking.Status status;
}
