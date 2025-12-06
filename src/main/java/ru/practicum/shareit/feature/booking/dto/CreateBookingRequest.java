package ru.practicum.shareit.feature.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@ToString
@Builder(toBuilder = true)
public class CreateBookingRequest {
    Long bookerId;

    @NotNull
    Long itemId;

    @NotNull
    @FutureOrPresent
    LocalDateTime start;

    @NotNull
    @Future
    LocalDateTime end;
}
