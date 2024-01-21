package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    private final FilmStorage filmStorage;
    @Autowired
    private final FilmService filmService;


    @GetMapping
    public List<Film> finAll() {
        return filmStorage.finAll();
    }

    @GetMapping("/{id}")
    public Film movieById(@PathVariable int id) {
        return filmStorage.movieById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return filmStorage.update(film);

    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addingLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.addingLike(id, userId);

    }

    @DeleteMapping("{id}/like/{userId}")
    public Integer removeLike(@PathVariable int id, @PathVariable int userId) {
        return filmService.removeLike(id, userId);
    }

    @GetMapping(value = {"popular?count={count}", "popular"})
    public Collection<Film> topFilms(@RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            return filmService.topFilms(count.get());
        } else {
            return filmService.topFilms(10);
        }
    }
}