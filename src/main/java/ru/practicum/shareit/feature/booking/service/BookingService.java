package ru.practicum.shareit.feature.booking.service;

import ru.practicum.shareit.feature.booking.BookingState;
import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.request.CreateBookingRequest;

import java.util.List;

public interface BookingService {
    BookingDto create(CreateBookingRequest request);

    BookingDto approve(Long bookingId, Long ownerId);

    BookingDto reject(Long bookingId, Long ownerId);

    BookingDto findByIdAndUserId(Long bookingId, Long userId);

    List<BookingDto> findByBooker(Long bookerId, BookingState state);

    List<BookingDto> findByOwner(Long ownerId, BookingState state);
}
