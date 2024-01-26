package ru.yandex.practicum.filmorate.exception;

public class ValidationExceptionUser extends RuntimeException {
    public ValidationExceptionUser(String message) {
        super(message);
    }

}
