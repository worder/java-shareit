package ru.practicum.shareit.feature.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.feature.booking.dto.BookingDto;
import ru.practicum.shareit.feature.booking.dto.request.CreateBookingRequest;
import ru.practicum.shareit.feature.booking.model.Booking;
import ru.practicum.shareit.feature.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void create_shouldReturnBooking() throws Exception {
        Long bookerId = 1L;

        CreateBookingRequest request = CreateBookingRequest.builder()
                .itemId(10L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDto response = BookingDto.builder()
                .id(1L)
                .status(Booking.Status.WAITING)
                .start(request.getStart())
                .end(request.getEnd())
                .build();

        Mockito.when(bookingService.create(Mockito.eq(bookerId), Mockito.any()))
                .thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .header(HEADER_USER_ID, bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    @Test
    void approve_shouldCallApprove_whenApprovedTrue() throws Exception {
        Long ownerId = 2L;
        Long bookingId = 1L;

        BookingDto response = BookingDto.builder()
                .id(bookingId)
                .status(Booking.Status.APPROVED)
                .build();

        Mockito.when(bookingService.approve(bookingId, ownerId))
                .thenReturn(response);

        mockMvc.perform(patch("/bookings/{id}", bookingId)
                        .header(HEADER_USER_ID, ownerId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void approve_shouldCallReject_whenApprovedFalse() throws Exception {
        Long ownerId = 2L;
        Long bookingId = 1L;

        BookingDto response = BookingDto.builder()
                .id(bookingId)
                .status(Booking.Status.REJECTED)
                .build();

        Mockito.when(bookingService.reject(bookingId, ownerId))
                .thenReturn(response);

        mockMvc.perform(patch("/bookings/{id}", bookingId)
                        .header(HEADER_USER_ID, ownerId)
                        .param("approved", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"));
    }

    @Test
    void getUserBookings_shouldReturnList() throws Exception {
        BookingDto dto = BookingDto.builder()
                .id(1L)
                .status(Booking.Status.APPROVED)
                .build();

        Mockito.when(bookingService.findByBooker(1L, BookingState.ALL))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/bookings")
                        .header(HEADER_USER_ID, 1L)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getOwnerBookings_shouldReturnList() throws Exception {
        BookingDto dto = BookingDto.builder()
                .id(2L)
                .status(Booking.Status.WAITING)
                .build();

        Mockito.when(bookingService.findByOwner(1L, BookingState.WAITING))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/bookings/owner")
                        .header(HEADER_USER_ID, 1L)
                        .param("state", "WAITING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getById_shouldReturnBooking() throws Exception {
        BookingDto dto = BookingDto.builder()
                .id(1L)
                .status(Booking.Status.APPROVED)
                .build();

        Mockito.when(bookingService.findByIdAndUserId(1L, 1L))
                .thenReturn(dto);

        mockMvc.perform(get("/bookings/{id}", 1L)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}
