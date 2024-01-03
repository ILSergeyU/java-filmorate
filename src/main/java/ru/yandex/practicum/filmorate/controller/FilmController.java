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
        films.add(film);
        return film;

    }

    @PutMapping("/films/{id}")
    public Film newFilms(@PathVariable int id, @Valid @RequestBody Film film) {
        if (films.isEmpty() || films.get(id) == null) {
            throw new ValidationException("The list Films is empty or not not this element");
        } else {
            films.set(id, film);
        }
        return film;
    }
}
