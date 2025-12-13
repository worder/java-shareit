package ru.practicum.shareit.feature.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.common.client.BaseClient;
import ru.practicum.shareit.feature.booking.dto.BookingState;
import ru.practicum.shareit.feature.booking.dto.CreateBookingRequest;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    ResponseEntity<Object> create(Long bookerId, CreateBookingRequest request) {
        return this.post("", bookerId, request);
    }

    ResponseEntity<Object> approve(Long bookingId, Long ownerId, Boolean approved) {
        return this.patch("/" + bookingId + "?approved={approved}", ownerId, Map.of("approved", approved), null);
    }

    ResponseEntity<Object> findByIdAndUserId(Long bookingId, Long userId) {
        return this.get("/" + bookingId, userId);
    }

    ResponseEntity<Object> findByBooker(Long bookerId, BookingState state) {
        return this.get("?state={state}", bookerId, Map.of("state", state.name()));
    }

    ResponseEntity<Object> findByOwner(Long ownerId, BookingState state) {
        return this.get("/owner?state={state}", ownerId, Map.of("state", state.name()));
    }
}
