package ru.practicum.shareit.feature.booking.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.feature.booking.model.Booking;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseBookingRepository extends BookingRepository, JpaRepository<Booking, Long> {
    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.id = :bookingId
                AND (b.booker.id = :userId OR b.item.owner.id = :userId)
            """)
    Optional<Booking> findByIdAndBookerIdOrOwnerId(@Param("bookingId") Long bookingId,
                                                   @Param("userId") Long userId);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :bookerId
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerId(@Param("bookerId") Long bookerId);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :bookerId AND b.status = :status
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerAndStatus(@Param("bookerId") Long bookerId,
                                        @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :bookerId AND b.status = :status
                AND (b.startDate <= CURRENT_TIMESTAMP AND b.endDate >= CURRENT_TIMESTAMP)
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerIdAndStatusInPresent(@Param("bookerId") Long bookerId,
                                                   @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :bookerId AND b.status = :status
                AND b.startDate > CURRENT_TIMESTAMP
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerIdAndStatusInFuture(@Param("bookerId") Long bookerId,
                                                  @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :bookerId AND b.status = :status
                AND b.endDate < CURRENT_TIMESTAMP
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByBookerIdAndStatusInPast(@Param("bookerId") Long bookerId,
                                                @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByItemOwnerId(@Param("ownerId") Long ownerId);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.status = :status
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByItemOwnerIdAndStatus(@Param("ownerId") Long ownerId,
                                             @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.status = :status
                AND (b.startDate <= CURRENT_TIMESTAMP AND b.endDate >= CURRENT_TIMESTAMP)
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByItemOwnerIdAndStatusInPresent(@Param("ownerId") Long ownerId,
                                                      @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.status = :status
                AND b.startDate > CURRENT_TIMESTAMP
            ORDER BY b.startDate DESC
            """)
    List<Booking> findByItemOwnerIdAndStatusInFuture(@Param("ownerId") Long ownerId,
                                                     @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId AND b.status = :status
                AND b.endDate < CURRENT_TIMESTAMP
            """)
    List<Booking> findByItemOwnerIdAndStatusInPast(@Param("ownerId") Long ownerId,
                                                   @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
                AND b.booker.id = :bookerId
                AND b.status = :status
                AND b.endDate < CURRENT_TIMESTAMP
            ORDER BY endDate DESC
            LIMIT 1
            """)
    Optional<Booking> findPastBookingByItemIdAndBookerIdAndStatus(@Param("itemId") Long itemId,
                                                                  @Param("bookerId") Long bookerId,
                                                                  @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
                AND b.status = :status
                AND b.endDate < CURRENT_TIMESTAMP
            ORDER BY b.endDate DESC
            LIMIT 1
            """)
    Optional<Booking> findLastBookingByItemId(@Param("itemId") Long itemId,
                                              @Param("status") Booking.Status status);

    @Override
    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.id = :itemId
                AND b.status = :status
                AND b.startDate > CURRENT_TIMESTAMP
            ORDER BY b.startDate ASC
            LIMIT 1
            """)
    Optional<Booking> findNextBookingForItemId(@Param("itemId") Long itemId,
                                               @Param("status") Booking.Status status);
}
