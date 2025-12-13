package ru.practicum.shareit.common.error;

import lombok.Data;
import lombok.Value;

@Value
public class ErrorResponse {
    String error;
    String details;
}
