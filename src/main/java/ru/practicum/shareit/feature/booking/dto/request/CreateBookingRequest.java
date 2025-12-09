package ru.practicum.shareit.feature.booking.dto.request;

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
    @FutureOrPresent(message = "Start date should be in in future or present")
    LocalDateTime start;

    @NotNull
    @Future(message = "End date should be in future")
    LocalDateTime end;
}
