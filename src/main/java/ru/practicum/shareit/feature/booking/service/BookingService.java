package ru.practicum.shareit.feature.booking.service;

import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.CreateBookingRequest;

public interface BookingService {
    BookingDto create(CreateBookingRequest request);
}
