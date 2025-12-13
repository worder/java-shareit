package ru.practicum.shareit.feature.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.request.CreateBookingRequest;
import ru.practicum.shareit.feature.booking.service.BookingServiceImpl;
import ru.practicum.shareit.feature.item.dto.CommentDto;
import ru.practicum.shareit.feature.item.dto.ItemDetailsDto;
import ru.practicum.shareit.feature.item.dto.ItemDto;
import ru.practicum.shareit.feature.item.dto.request.CreateCommentRequest;
import ru.practicum.shareit.feature.item.dto.request.CreateItemRequest;
import ru.practicum.shareit.feature.item.dto.request.UpdateItemRequest;
import ru.practicum.shareit.feature.user.dto.UserDto;
import ru.practicum.shareit.feature.user.dto.request.CreateUserRequest;
import ru.practicum.shareit.feature.user.service.UserServiceImpl;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {
    @Autowired
    ItemServiceImpl itemService;

    @Autowired
    UserServiceImpl userService;

    @Autowired
    BookingServiceImpl bookingService;

    static Long BAD_ID = 999L;

    CreateUserRequest userRequest = new CreateUserRequest("John", "john@example.com");
    CreateUserRequest user2Request = new CreateUserRequest("Bob", "bob@example.com");

    CreateItemRequest itemRequest = new CreateItemRequest("Test item", "Item desc", true, null);

    CreateCommentRequest commentRequest = new CreateCommentRequest("Comment 1");

    @Test
    void create_ShouldCreate() {
        UserDto user = userService.create(userRequest);
        ItemDto item = itemService.create(user.getId(), itemRequest);

        assertNotNull(item.getId());
    }

    @Test
    void create_ShouldThrowException_UserNotFound() {
        assertThrows(NotFoundException.class, () -> itemService.create(BAD_ID, itemRequest));
    }

    @Test
    void create_ShouldThrowException_RequestNotFound() {
        CreateItemRequest itemRequestWithRequestId = itemRequest.toBuilder().requestId(BAD_ID).build();

        UserDto user = userService.create(userRequest);
        assertThrows(NotFoundException.class, () -> itemService.create(user.getId(), itemRequestWithRequestId));
    }

    @Test
    void findById_ReturnsItemWithComments() {
        // create item
        UserDto owner = userService.create(userRequest);
        ItemDto item = itemService.create(owner.getId(), itemRequest);

        UserDto booker = userService.create(user2Request);
        // create booking in past
        BookingDto booking = bookingService.create(booker.getId(), new CreateBookingRequest(
                item.getId(),
                LocalDateTime.now().minus(Duration.ofHours(2)),
                LocalDateTime.now().minus(Duration.ofHours(1))
        ));
        bookingService.approve(booking.getId(), owner.getId());

        // add comment to item
        itemService.createComment(booker.getId(), item.getId(), commentRequest);

        assertEquals(1, itemService.findById(booker.getId(), item.getId()).getComments().size());
        assertEquals(1, itemService.findById(owner.getId(), item.getId()).getComments().size());
    }

    @Test
    void findById_ReturnsItemWithBookingDates_ForOwner() {
        // create item
        UserDto owner = userService.create(userRequest);
        ItemDto item = itemService.create(owner.getId(), itemRequest);

        UserDto booker = userService.create(user2Request);
        // create booking in past
        BookingDto pastBooking = bookingService.create(booker.getId(), new CreateBookingRequest(
                item.getId(),
                LocalDateTime.now().minus(Duration.ofHours(2)),
                LocalDateTime.now().minus(Duration.ofHours(1))
        ));
        bookingService.approve(pastBooking.getId(), owner.getId());

        // create booking in future
        BookingDto futureBooking = bookingService.create(booker.getId(), new CreateBookingRequest(
                item.getId(),
                LocalDateTime.now().plus(Duration.ofHours(1)),
                LocalDateTime.now().plus(Duration.ofHours(2))
        ));
        bookingService.approve(futureBooking.getId(), owner.getId());

        // dates for owner
        assertEquals(pastBooking.getEnd(),
                itemService.findById(owner.getId(), item.getId()).getLastBooking().getEnd());
        assertEquals(futureBooking.getStart(),
                itemService.findById(owner.getId(), item.getId()).getNextBooking().getStart());

        // null for booker
        assertNull(itemService.findById(booker.getId(), item.getId()).getLastBooking());
        assertNull(itemService.findById(booker.getId(), item.getId()).getNextBooking());
    }

    @Test
    void update_UpdatesItem() {
        UserDto owner = userService.create(userRequest);
        ItemDto item = itemService.create(owner.getId(), itemRequest);

        UpdateItemRequest updateRequest = new UpdateItemRequest("new_name", "new_desc", !itemRequest.getAvailable());
        itemService.update(owner.getId(), item.getId(), updateRequest);

        ItemDetailsDto result = itemService.findById(owner.getId(), item.getId());

        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getDescription(), result.getDescription());
        assertEquals(updateRequest.getAvailable(), result.getAvailable());
    }

    @Test
    void update_ThrowsException_WrongOwner() {
        UserDto owner = userService.create(userRequest);
        ItemDto item = itemService.create(owner.getId(), itemRequest);

        UpdateItemRequest updateRequest = new UpdateItemRequest("new_name", "new_desc", !itemRequest.getAvailable());

        assertThrows(NotFoundException.class, () -> itemService.update(BAD_ID, item.getId(), updateRequest));
    }

    @Test
    void update_ThrowsException_WrongItem() {
        UserDto owner = userService.create(userRequest);
        itemService.create(owner.getId(), itemRequest);

        UpdateItemRequest updateRequest = new UpdateItemRequest("new_name", "new_desc", !itemRequest.getAvailable());

        assertThrows(NotFoundException.class, () -> itemService.update(owner.getId(), BAD_ID, updateRequest));
    }

    @Test
    void delete_DeletesItem() {
        UserDto owner = userService.create(userRequest);
        ItemDto item = itemService.create(owner.getId(), itemRequest);

        itemService.delete(owner.getId(), item.getId());

        assertThrows(NotFoundException.class, () -> itemService.findById(owner.getId(), item.getId()));
    }

    @Test
    void delete_ThrowsException_NoSuchItem() {
        UserDto user = userService.create(userRequest);
        assertThrows(NotFoundException.class, () -> itemService.delete(user.getId(), BAD_ID));
    }

    @Test
    void getUserItems_ReturnsUserItems() {
        UserDto owner = userService.create(userRequest);
        UserDto justBobOwnsNothing = userService.create(user2Request);

        itemService.create(owner.getId(), itemRequest);

        assertEquals(1, itemService.getUserItems(owner.getId()).size());
        assertEquals(0, itemService.getUserItems(justBobOwnsNothing.getId()).size());
    }

    @Test
    void findUserItems_SearchIgnoresCase() {
        UserDto owner = userService.create(userRequest);
        itemService.create(owner.getId(), itemRequest);

        assertEquals(1, itemService.findUserItems(owner.getId(), itemRequest.getDescription().toLowerCase()).size());
        assertEquals(1, itemService.findUserItems(owner.getId(), itemRequest.getName().toLowerCase()).size());
        assertEquals(0, itemService.findUserItems(owner.getId(), "nothing").size());
    }

    @Test
    void findUserItems_EmptyResult_ForEmptySearchText() {
        UserDto owner = userService.create(userRequest);
        itemService.create(owner.getId(), itemRequest);

        assertEquals(0, itemService.findUserItems(owner.getId(), "").size());
    }

    @Test
    void createComment_CreatesCommentSuccessfully() {
        // create item
        UserDto owner = userService.create(userRequest);
        ItemDto item = itemService.create(owner.getId(), itemRequest);

        UserDto booker = userService.create(user2Request);
        // create booking in past
        BookingDto booking = bookingService.create(booker.getId(), new CreateBookingRequest(
                item.getId(),
                LocalDateTime.now().minus(Duration.ofHours(2)),
                LocalDateTime.now().minus(Duration.ofHours(1))
        ));
        bookingService.approve(booking.getId(), owner.getId());

        // add comment to item
        CommentDto comment = itemService.createComment(booker.getId(), item.getId(), commentRequest);

        assertEquals(
                commentRequest.text(),
                itemService.findById(booker.getId(), item.getId()).getComments().getFirst().getText());
    }
}
