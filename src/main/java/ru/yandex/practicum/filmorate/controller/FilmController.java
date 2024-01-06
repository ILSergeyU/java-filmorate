package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
public class FilmController {

    private List<Film> films = new LinkedList<>();

    @GetMapping("/films")
    public List<Film> finAll() {

        return films;
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        try {
            film.setId(films.size() + 1);
            film.setReleaseDate(film.getReleaseDate());
            log.info("Добавлен фильм: {}", film);
            films.add(film);

        } catch (ValidationException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
        }
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        try {
            if (films.isEmpty() || films.get(film.getId() - 1) == null) {
                throw new ValidationException("The list Films is empty or not not this element");

            } else {
                films.set(film.getId() - 1, film);
                log.info("Фильм {} с id:{} обновлен ", film, film.getId());
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
        }
        return film;
    }
}
