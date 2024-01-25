package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

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
    private final FilmService filmService;


    @GetMapping
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable int id) {
        log.info("Поиск фильма по Id: {} ", id);
        return filmService.findFilmById(id);
    }

    @PostMapping

    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Создал фильм: {} ", film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновил фильм: {} ", film);
        return filmService.updateFilm(film);

    }

    @PutMapping("/{id}/like/{userId}")
    public Integer addingLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь ставит лайк");
        return filmService.addingLike(id, userId);

    }

    @DeleteMapping("{id}/like/{userId}")
    public Integer removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь удалил лайк");
        return filmService.removeLike(id, userId);
    }

    @GetMapping(value = {"popular?count={count}", "popular"})
    public Collection<Film> topFilms(@RequestParam Optional<Integer> count) {
        log.info("Сортировка фильмов");
        if (count.isPresent()) {
            return filmService.topFilms(count.get());
        } else {
            return filmService.topFilms(10);
        }
    }
}