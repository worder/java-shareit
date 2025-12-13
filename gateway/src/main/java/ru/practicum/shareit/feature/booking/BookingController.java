package ru.practicum.shareit.feature.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.feature.booking.dto.BookingState;
import ru.practicum.shareit.feature.booking.dto.CreateBookingRequest;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HEADER_USER_ID) Long bookerId,
                                         @Valid @RequestBody CreateBookingRequest request) {
        return bookingClient.create(bookerId, request);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                          @PathVariable Long bookingId,
                                          @RequestParam(name = "approved") Boolean approved) {
        return bookingClient.approve(bookingId, ownerId, approved);
    }

    @GetMapping
    public ResponseEntity<Object> getUserBookings(
            @RequestHeader(HEADER_USER_ID) Long bookerId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam) {

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        return bookingClient.findByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(
            @RequestHeader(HEADER_USER_ID) Long ownerId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam) {

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        return bookingClient.findByOwner(ownerId, state);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader(HEADER_USER_ID) Long userId,
                                          @PathVariable Long bookingId) {
        return bookingClient.findByIdAndUserId(bookingId, userId);
    }
}
