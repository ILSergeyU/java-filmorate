package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {

    private List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> finAll() {

        return films;
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(films.size() + 1);
        films.add(film);
        return film;

    }

    @PutMapping("/films")
    public Film newFilms(@Valid @RequestBody Film film) {
        if (films.isEmpty() || films.get(film.getId() - 1) == null) {
            throw new ValidationException("The list Films is empty or not not this element");
        } else {
            films.set(film.getId() - 1, film);
        }
        return film;
    }
}
