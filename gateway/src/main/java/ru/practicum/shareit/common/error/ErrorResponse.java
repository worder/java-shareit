package ru.practicum.shareit.common.error;

import lombok.Value;

@Value
public class ErrorResponse {
    String error;
    String details;
}
