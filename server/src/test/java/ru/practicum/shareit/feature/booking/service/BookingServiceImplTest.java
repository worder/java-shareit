package ru.practicum.shareit.feature.booking.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.exception.BadRequestException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.booking.BookingState;
import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.request.CreateBookingRequest;
import ru.practicum.shareit.feature.booking.model.Booking;
import ru.practicum.shareit.feature.item.dto.ItemDto;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.service.ItemService;
import ru.practicum.shareit.feature.user.dto.UserDto;
import ru.practicum.shareit.feature.user.dto.request.CreateUserRequest;
import ru.practicum.shareit.feature.user.service.UserService;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImplTest {

    @Autowired
    BookingServiceImpl bookingService;

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    static Long BAD_ID = 999L;

    CreateUserRequest userRequest = new CreateUserRequest("John", "john@example.com");
    CreateUserRequest user2Request = new CreateUserRequest("Bob", "bob@example.com");

    CreateItemRequest itemRequest = new CreateItemRequest("Test item", "Item desc", true, null);

    UserDto owner;
    UserDto booker;
    ItemDto item;

    LocalDateTime start = LocalDateTime.now().minus(Duration.ofHours(2));
    LocalDateTime end = LocalDateTime.now().minus(Duration.ofHours(1));

    @BeforeEach
    void prepareOwnerAndItem() {
        this.owner = userService.create(userRequest);
        this.booker = userService.create(user2Request);
        this.item = itemService.create(owner.getId(), itemRequest);
    }

    private static CreateBookingRequest createBookingRequestForItemId(Long itemId) {
        return new CreateBookingRequest(
                itemId,
                LocalDateTime.now().minus(Duration.ofHours(2)),
                LocalDateTime.now().minus(Duration.ofHours(1))
        );
    }

    private BookingDto createBookingForState(BookingState state) {
        CreateBookingRequest pastBooking = new CreateBookingRequest(
                this.item.getId(),
                LocalDateTime.now().minus(Duration.ofHours(2)),
                LocalDateTime.now().minus(Duration.ofHours(1)));

        CreateBookingRequest currentBooking = new CreateBookingRequest(
                this.item.getId(),
                LocalDateTime.now().minus(Duration.ofHours(1)),
                LocalDateTime.now().plus(Duration.ofHours(1)));

        CreateBookingRequest futureBooking = new CreateBookingRequest(
                this.item.getId(),
                LocalDateTime.now().plus(Duration.ofHours(1)),
                LocalDateTime.now().plus(Duration.ofHours(2)));

        BookingDto booking;

        switch (state) {
            case CURRENT:
                booking = bookingService.create(booker.getId(), currentBooking);
                bookingService.approve(booking.getId(), owner.getId());
                break;
            case FUTURE:
                booking = bookingService.create(booker.getId(), futureBooking);
                bookingService.approve(booking.getId(), owner.getId());
                break;
            case PAST:
                booking = bookingService.create(booker.getId(), pastBooking);
                bookingService.approve(booking.getId(), owner.getId());
                break;
            case REJECTED:
                booking = bookingService.create(booker.getId(), pastBooking);
                bookingService.reject(booking.getId(), owner.getId());
                break;
            case ALL:
            case WAITING:
            default:
                booking = bookingService.create(booker.getId(), pastBooking);
                break;
        }

        return booking;
    }

    @Test
    void create_ShouldCreateBooking() {
        BookingDto booking = bookingService.create(booker.getId(), createBookingRequestForItemId(item.getId()));
        assertEquals(booking.getId(), bookingService.findByIdAndUserId(booking.getId(), booker.getId()).getId());
    }

    @Test
    void create_ShouldThrowException_NoUser() {
        assertThrows(NotFoundException.class, () ->
                bookingService.create(BAD_ID, createBookingRequestForItemId(item.getId()))
        );

    }

    @Test
    void create_ShouldThrowException_NoItem() {
        assertThrows(NotFoundException.class, () ->
                bookingService.create(booker.getId(), createBookingRequestForItemId(BAD_ID))
        );
    }

    @Test
    void create_ShouldThrowException_StartDateAheadOfEndDate() {
        CreateBookingRequest invalidDatesRequest = new CreateBookingRequest(item.getId(), end, start);
        assertThrows(BadRequestException.class, () -> bookingService.create(booker.getId(), invalidDatesRequest));
    }

    @Test
    void create_ShouldThrowException_itemNotAvailable() {
        ItemDto unavailableItem = itemService.create(
                owner.getId(),
                itemRequest.toBuilder().available(false).build());

        assertThrows(BadRequestException.class,
                () -> bookingService.create(booker.getId(), createBookingRequestForItemId(unavailableItem.getId())));
    }

    @Test
    void approve_ApprovesBookingByOwner() {
        BookingDto booking = bookingService.create(booker.getId(), createBookingRequestForItemId(item.getId()));
        bookingService.approve(booking.getId(), owner.getId());

        assertEquals(
                Booking.Status.APPROVED,
                bookingService.findByIdAndUserId(booking.getId(), owner.getId()).getStatus()
        );
    }

    @Test
    void reject_RejectsBookingByOwner() {
        BookingDto booking = bookingService.create(booker.getId(), createBookingRequestForItemId(item.getId()));
        bookingService.reject(booking.getId(), owner.getId());

        assertEquals(
                Booking.Status.REJECTED,
                bookingService.findByIdAndUserId(booking.getId(), owner.getId()).getStatus()
        );
    }

    @Test
    void findByIdAndUserId_FindsBookingByInvolvedUsersIds() {
        BookingDto booking = bookingService.create(booker.getId(), createBookingRequestForItemId(item.getId()));

        assertEquals(booking.getId(), bookingService
                .findByIdAndUserId(booking.getId(), owner.getId())
                .getId());
        assertEquals(booking.getId(), bookingService
                .findByIdAndUserId(booking.getId(), booker.getId())
                .getId());
    }

    @Test
    void findByBooker_AllStates() {
        createBookingForState(BookingState.CURRENT);
        assertEquals(1, bookingService.findByBooker(booker.getId(), BookingState.CURRENT).size());

        createBookingForState(BookingState.PAST);
        assertEquals(1, bookingService.findByBooker(booker.getId(), BookingState.PAST).size());

        createBookingForState(BookingState.FUTURE);
        assertEquals(1, bookingService.findByBooker(booker.getId(), BookingState.FUTURE).size());

        createBookingForState(BookingState.WAITING);
        assertEquals(1, bookingService.findByBooker(booker.getId(), BookingState.WAITING).size());

        createBookingForState(BookingState.REJECTED);
        assertEquals(1, bookingService.findByBooker(booker.getId(), BookingState.REJECTED).size());

        createBookingForState(BookingState.ALL);
        assertEquals(6, bookingService.findByBooker(booker.getId(), BookingState.ALL).size());
    }

    @Test
    void findByBooker_ThrowsException_BadUser() {
        createBookingForState(BookingState.CURRENT);
        assertThrows(NotFoundException.class, () -> bookingService.findByBooker(BAD_ID, BookingState.CURRENT));
    }

    @Test
    void findByOwner_AllStates() {
        createBookingForState(BookingState.CURRENT);
        assertEquals(1, bookingService.findByOwner(owner.getId(), BookingState.CURRENT).size());

        createBookingForState(BookingState.PAST);
        assertEquals(1, bookingService.findByOwner(owner.getId(), BookingState.PAST).size());

        createBookingForState(BookingState.FUTURE);
        assertEquals(1, bookingService.findByOwner(owner.getId(), BookingState.FUTURE).size());

        createBookingForState(BookingState.WAITING);
        assertEquals(1, bookingService.findByOwner(owner.getId(), BookingState.WAITING).size());

        createBookingForState(BookingState.REJECTED);
        assertEquals(1, bookingService.findByOwner(owner.getId(), BookingState.REJECTED).size());

        createBookingForState(BookingState.ALL);
        assertEquals(6, bookingService.findByOwner(owner.getId(), BookingState.ALL).size());
    }

    @Test
    void findByOwner_ThrowsException_BadUser() {
        createBookingForState(BookingState.CURRENT);
        assertThrows(NotFoundException.class, () -> bookingService.findByOwner(BAD_ID, BookingState.CURRENT));
    }

}
