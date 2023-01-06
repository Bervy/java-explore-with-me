package ru.practicum.explore.error;

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}