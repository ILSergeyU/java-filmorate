package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationExpensionFilms;

import java.util.Map;

@Slf4j
@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notId(final IncorrectCountException e) {
        log.info("Возникло исключение: {}", e.getMessage());
        return Map.of("error", "Ошибка Id",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> negativeId(final ValidationException e) {
        log.info("Возникло исключение: {}", e.getMessage());
        return Map.of("error", "Ошибка Id",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> negativeFilmId(final ValidationExpensionFilms e) {
        log.info("Возникло исключение: {}", e.getMessage());
        return Map.of("error", "ОшибкаFilms Id",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> exseption(final Throwable e) {
        log.info("Возникло исключение: {}", e.getMessage());
        return Map.of("error", "ОшибкаFilms Id",
                "errorMessage", e.getMessage());
    }


}
