package ru.practicum.shareit.feature.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import lombok.Value;

import java.time.LocalDate;

@Value
@ToString
public class UpdateBookingRequest {
    Long bookerId;

    @NotNull
    Long itemId;

    @NotNull
    LocalDate start;

    @NotNull
    LocalDate end;
}
