package ru.practicum.shareit.feature.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.booking.Booking;
import ru.practicum.shareit.feature.booking.dal.BookingRepository;
import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.BookingMapper;
import ru.practicum.shareit.feature.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.feature.booking.dto.UpdateBookingRequest;
import ru.practicum.shareit.feature.item.Item;
import ru.practicum.shareit.feature.item.dal.ItemRepository;
import ru.practicum.shareit.feature.user.User;
import ru.practicum.shareit.feature.user.dal.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
}
