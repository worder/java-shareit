package ru.practicum.shareit.common.exception;

public class ConflictEntity extends RuntimeException {
    public ConflictEntity(String message) {
        super(message);
    }
}
