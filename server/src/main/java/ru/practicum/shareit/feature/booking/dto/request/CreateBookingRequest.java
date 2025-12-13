package ru.practicum.shareit.feature.booking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@AllArgsConstructor
@Value
@ToString
@Builder(toBuilder = true)
public class CreateBookingRequest {
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
