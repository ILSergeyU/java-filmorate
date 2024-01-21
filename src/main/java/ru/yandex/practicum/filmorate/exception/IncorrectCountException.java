package ru.yandex.practicum.filmorate.exception;

public class IncorrectCountException extends RuntimeException {
    public IncorrectCountException() {
    }

    public IncorrectCountException(String message) {
        super(message);
    }
}
