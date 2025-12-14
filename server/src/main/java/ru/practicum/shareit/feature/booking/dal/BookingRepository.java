package ru.practicum.shareit.feature.booking.dal;

import ru.practicum.shareit.feature.booking.model.Booking;

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


    Optional<Booking> findPastBookingByItemIdAndBookerIdAndStatus(Long itemId, Long bookerId, Booking.Status status);

    Optional<Booking> findLastBookingByItemId(Long itemId, Booking.Status status);

    Optional<Booking> findNextBookingForItemId(Long itemId, Booking.Status status);
}
