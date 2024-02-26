package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import javax.validation.ValidationException;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Iterable<Genre> getGenres() {
        return genreService.getAll();
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        if (id > 6) {
            throw new ValidationException("Значение больше имеющегося");
        }
        return genreService.get(id);
    }
}