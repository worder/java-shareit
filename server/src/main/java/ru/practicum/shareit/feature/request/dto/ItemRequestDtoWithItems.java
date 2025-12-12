package ru.practicum.shareit.feature.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.feature.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder
public class ItemRequestDtoWithItems {
    Long id;
    String description;
    UserDto requestor;
    LocalDateTime created;
    List<ResponseItemDao> items;

    @Value
    @Builder
    public static class ResponseItemDao {
        Long itemId;
        Long ownerId;
        String name;
    }
}
