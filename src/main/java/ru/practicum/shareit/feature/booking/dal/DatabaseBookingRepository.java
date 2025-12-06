package ru.practicum.shareit.feature.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.feature.booking.Booking;

@Repository
public interface DatabaseBookingRepository extends BookingRepository, JpaRepository<Booking, Long> {
}
