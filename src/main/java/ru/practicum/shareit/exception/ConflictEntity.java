package ru.practicum.shareit.exception;

public class ConflictEntity extends RuntimeException {
    public ConflictEntity(String message) {
        super(message);
    }
}
