package ru.practicum.shareit.feature.booking.dto;

import ru.practicum.shareit.feature.booking.Booking;
import ru.practicum.shareit.feature.item.dto.ItemMapper;
import ru.practicum.shareit.feature.user.dto.UserMapper;

public class BookingMapper {
    public static Booking mapToModel(CreateBookingRequest request) {
        Booking booking = new Booking();
        booking.setStatus(Booking.Status.WAITING);
        booking.setStartDate(request.getStart());
        booking.setEndDate(request.getEnd());
        return booking;
    }

    public static BookingDto mapToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .item(ItemMapper.mapToDto(booking.getItem()))
                .booker(UserMapper.mapToDto(booking.getBooker()))
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(booking.getStatus())
                .build();
    }
}
