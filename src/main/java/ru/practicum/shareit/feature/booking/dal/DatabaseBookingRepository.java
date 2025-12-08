package ru.practicum.shareit.feature.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.feature.booking.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseBookingRepository extends BookingRepository, JpaRepository<Booking, Long> {
    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.id = ?1
                AND (b.booker.id = ?2 OR b.item.owner.id = ?2)
            """)
    Optional<Booking> findByIdAndBookerIdOrOwnerId(Long bookingId, Long userId);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerId(Long bookerId);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1 AND b.status = ?2
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerAndStatus(Long bookerId, Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1 AND b.status = ?2
                AND (b.startDate <= CURRENT_DATE AND b.endDate >= CURRENT_DATE)
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerIdAndStatusInPresent(Long bookerId, Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1 AND b.status = ?2
                AND b.startDate > CURRENT_DATE
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerIdAndStatusInFuture(Long ownerId, Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = ?1 AND b.status = ?2
                AND b.endDate < CURRENT_DATE
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerIdAndStatusInPast(Long ownerId, Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = ?1
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByItemOwnerId(Long ownerId);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = ?1 AND b.status = ?2
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByItemOwnerIdAndStatus(Long ownerId, Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = ?1 AND b.status = ?2
                AND (b.startDate <= CURRENT_DATE AND b.endDate >= CURRENT_DATE)
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByItemOwnerIdAndStatusInPresent(Long ownerId, Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = ?1 AND b.status = ?2
                AND b.startDate > CURRENT_DATE
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByItemOwnerIdAndStatusInFuture(Long ownerId, Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = ?1 AND b.status = ?2
                AND b.endDate < CURRENT_DATE
            """)
    List<Booking> findByItemOwnerIdAndStatusInPast(Long ownerId, Booking.Status status);
}
