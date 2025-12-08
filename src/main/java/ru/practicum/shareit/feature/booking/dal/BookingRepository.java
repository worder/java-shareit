package ru.practicum.shareit.feature.booking.dal;

import ru.practicum.shareit.feature.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Booking save(Booking booking);

    Optional<Booking> findById(Long id);

    Optional<Booking> findByIdAndBookerIdOrOwnerId(Long bookingId, Long userId);


    List<Booking> findByBookerId(Long bookerId);

    List<Booking> findByBookerAndStatus(Long bookerId, Booking.Status status);

    List<Booking> findByBookerIdAndStatusInPresent(Long bookerId, Booking.Status status);

    List<Booking> findByBookerIdAndStatusInFuture(Long bookerId, Booking.Status status);

    List<Booking> findByBookerIdAndStatusInPast(Long bookerId, Booking.Status status);


    List<Booking> findByItemOwnerId(Long ownerId);

    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, Booking.Status status);

    List<Booking> findByItemOwnerIdAndStatusInPresent(Long ownerId, Booking.Status status);

    List<Booking> findByItemOwnerIdAndStatusInFuture(Long ownerId, Booking.Status status);

    List<Booking> findByItemOwnerIdAndStatusInPast(Long ownerId, Booking.Status status);
}
