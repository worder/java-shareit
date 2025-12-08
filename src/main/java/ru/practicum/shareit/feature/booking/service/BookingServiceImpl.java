package ru.practicum.shareit.feature.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.booking.Booking;
import ru.practicum.shareit.feature.booking.BookingState;
import ru.practicum.shareit.feature.booking.dal.BookingRepository;
import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.BookingMapper;
import ru.practicum.shareit.feature.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.feature.item.Item;
import ru.practicum.shareit.feature.item.dal.ItemRepository;
import ru.practicum.shareit.feature.user.User;
import ru.practicum.shareit.feature.user.dal.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userStorage;
    private final ItemRepository itemStorage;
    private final BookingRepository bookingStorage;

    @Override
    public BookingDto create(CreateBookingRequest request) {
        Item item = itemStorage.findById(request.getItemId()).orElseThrow(() ->
                new NotFoundException("item not found"));
        User booker = userStorage.findById(request.getBookerId()).orElseThrow(() ->
                new NotFoundException("User not found"));

        Booking booking = BookingMapper.mapToModel(request);
        booking.setItem(item);
        booking.setBooker(booker);

        if (booking.getStartDate().isAfter(booking.getEndDate())
                || booking.getStartDate().isEqual(booking.getEndDate())) {
            throw new BadRequestException("Start date must be before end date");
        }
        if (booking.getStartDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Start date must not be in the past");
        }
        if (!item.getAvailable()) {
            throw new BadRequestException("Item is not available");
        }

        booking = bookingStorage.save(booking);
        return BookingMapper.mapToDto(booking);
    }

    @Override
    public BookingDto approve(Long bookingId, Long ownerId) {
        return this.updateStatus(bookingId, ownerId, Booking.Status.APPROVED);
    }

    @Override
    public BookingDto reject(Long bookingId, Long ownerId) {
        return this.updateStatus(bookingId, ownerId, Booking.Status.REJECTED);
    }

    @Override
    public BookingDto findByIdAndUserId(Long bookingId, Long userId) {
        return bookingStorage.findByIdAndBookerIdOrOwnerId(bookingId, userId)
                .map(BookingMapper::mapToDto)
                .orElseThrow(() -> new NotFoundException("Booking not found"));
    }

    private BookingDto updateStatus(Long bookingId, Long ownerId, Booking.Status status) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (!booking.getItem().getOwner().getId().equals(ownerId)) {
            throw new BadRequestException("Incorrect owner id");
        }

        booking.setStatus(status);
        return BookingMapper.mapToDto(bookingStorage.save(booking));
    }

    @Override
    public List<BookingDto> findByBooker(Long bookerId, BookingState state) {
        return (switch (state) {
            case CURRENT -> bookingStorage.findByBookerIdAndStatusInPresent(bookerId, Booking.Status.APPROVED);
            case PAST -> bookingStorage.findByBookerIdAndStatusInPast(bookerId, Booking.Status.APPROVED);
            case FUTURE -> bookingStorage.findByBookerIdAndStatusInFuture(bookerId, Booking.Status.APPROVED);
            case WAITING -> bookingStorage.findByBookerAndStatus(bookerId, Booking.Status.WAITING);
            case REJECTED -> bookingStorage.findByBookerAndStatus(bookerId, Booking.Status.REJECTED);
            case null, default -> bookingStorage.findByBookerId(bookerId);
        }).stream().map(BookingMapper::mapToDto).toList();
    }

    @Override
    public List<BookingDto> findByOwner(Long ownerId, BookingState state) {
        return (switch (state) {
            case CURRENT -> bookingStorage.findByItemOwnerIdAndStatusInPresent(ownerId, Booking.Status.APPROVED);
            case PAST -> bookingStorage.findByItemOwnerIdAndStatusInPast(ownerId, Booking.Status.APPROVED);
            case FUTURE -> bookingStorage.findByItemOwnerIdAndStatusInFuture(ownerId, Booking.Status.APPROVED);
            case WAITING -> bookingStorage.findByItemOwnerIdAndStatus(ownerId, Booking.Status.WAITING);
            case REJECTED -> bookingStorage.findByItemOwnerIdAndStatus(ownerId, Booking.Status.REJECTED);
            case null, default -> bookingStorage.findByItemOwnerId(ownerId);
        }).stream().map(BookingMapper::mapToDto).toList();
    }
}
