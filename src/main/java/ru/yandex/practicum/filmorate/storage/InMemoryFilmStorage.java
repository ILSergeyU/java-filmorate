package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.ValidationExpensionFilms;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Data
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        log.info("Запрос на получение списка всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new ValidationExpensionFilms("Нет фильма по такому Id");
        }
        log.info("Запрос на получение фильма по Id");
        return films.get(id);


    }

    @Override
    public Film createFilm(Film film) {
        try {
            film.setId(films.size() + 1);
            film.setReleaseDate(film.getReleaseDate());
            log.info("Film created: {}", film);
            films.put(films.size() + 1, film);
        } catch (ValidationException e) {
            log.error("Film creation error: {}", e.getMessage());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.isEmpty() || films.get(film.getId()) == null) {
            System.out.println("Film with id " + film.getId() + " not found in the collection.");
            throw new ValidationException("The list Films is empty or not not this element");

        } else {
            films.put(film.getId(), film);
            log.info("Film {} updated with id: {}", film, film.getId());
        }
        return film;
    }

}

