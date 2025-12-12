package ru.practicum.shareit.feature.booking.dto.request;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@ToString
@Builder(toBuilder = true)
public class CreateBookingRequest {
    Long bookerId;
    Long itemId;
    LocalDateTime start;
    LocalDateTime end;
}
