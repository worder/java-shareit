package ru.practicum.shareit.feature.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.request.CreateBookingRequest;
import ru.practicum.shareit.feature.booking.service.BookingService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(HEADER_USER_ID) Long bookerId,
                             @RequestBody CreateBookingRequest request) {
        return bookingService.create(bookerId, request);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader(HEADER_USER_ID) Long ownerId,
                              @PathVariable Long bookingId,
                              @RequestParam(name = "approved") Boolean approved) {
        if (approved) {
            return bookingService.approve(bookingId, ownerId);
        } else {
            return bookingService.reject(bookingId, ownerId);
        }
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader(HEADER_USER_ID) Long bookerId,
                                            @RequestParam(required = false) BookingState state) {
        return bookingService.findByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                             @RequestParam(required = false) BookingState state) {
        return bookingService.findByOwner(ownerId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(HEADER_USER_ID) Long userId,
                              @PathVariable Long bookingId) {
        return bookingService.findByIdAndUserId(bookingId, userId);
    }
}
