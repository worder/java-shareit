package ru.practicum.shareit.feature.booking.dal;

import ru.practicum.shareit.feature.booking.Booking;

public interface BookingRepository {
    Booking save(Booking booking);
}
