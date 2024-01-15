package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> finAll() {
        return new ArrayList<>(films.values());

    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        try {
            film.setId(films.size() + 1);
            film.setReleaseDate(film.getReleaseDate());
            log.info("Добавлен фильм: {}", film);
            films.put(films.size() + 1, film);
        } catch (ValidationException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
        }
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (films.isEmpty() || films.get(film.getId()) == null) {
            throw new ValidationException("The list Films is empty or not not this element");

        } else {
            films.put(film.getId()/* - 1*/, film);
            log.info("Фильм {} с id:{} обновлен ", film, film.getId());
        }
        return film;

    }
}
