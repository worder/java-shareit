package ru.practicum.shareit.feature.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.feature.booking.service.BookingService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    BookingDto create(@RequestHeader(HEADER_USER_ID) Long userId,
                      @RequestBody @Valid CreateBookingRequest request) {
        return bookingService.create(request.toBuilder().bookerId(userId).build());
    }
}
