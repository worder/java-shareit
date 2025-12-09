package ru.practicum.shareit.feature.item.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDateDto {
    LocalDateTime start;
    LocalDateTime end;
}
