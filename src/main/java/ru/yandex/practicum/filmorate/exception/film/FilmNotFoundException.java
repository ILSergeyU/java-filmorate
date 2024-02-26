package ru.yandex.practicum.filmorate.exception.film;

import ru.yandex.practicum.filmorate.exception.NotFoundException;

public class FilmNotFoundException extends NotFoundException {
    public static final String FILM_NOT_FOUND = "Фильм ID_%d не найден";

    public FilmNotFoundException(String message) {
        super(message);
    }
}