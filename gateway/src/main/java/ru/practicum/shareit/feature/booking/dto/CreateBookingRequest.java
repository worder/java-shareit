package ru.practicum.shareit.feature.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@ToString
public class CreateBookingRequest {
    @NotNull
    Long itemId;

    @NotNull
    @FutureOrPresent(message = "Start date should be in in future or present")
    LocalDateTime start;

    @NotNull
    @Future(message = "End date should be in future")
    LocalDateTime end;
}
