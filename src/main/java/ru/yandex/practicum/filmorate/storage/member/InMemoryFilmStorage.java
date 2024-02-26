package ru.yandex.practicum.filmorate.storage.member;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionFilms;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionUser;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmStorage;

import java.util.*;

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
            throw new ValidationExceptionFilms("Нет фильма по такому Id");
        }
        log.info("Запрос на получение фильма по Id");
        return films.get(id);


    }

    @Override
    public void addGenres(int filmID, Set<Genre> genres) {

    }

    @Override
    public void updateGenres(int filmID, Set<Genre> genres) {

    }

    @Override
    public Set<Genre> getGenres(int filmID) {
        return null;
    }



    @Override
    public Film createFilm(Film film) {
        try {
            film.setId(films.size() + 1);
            film.setReleaseDate(film.getReleaseDate());
            log.info("Film created: {}", film);
            films.put(films.size() + 1, film);
        } catch (ValidationExceptionUser e) {
            log.error("Film creation error: {}", e.getMessage());
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.isEmpty() || films.get(film.getId()) == null) {
            System.out.println("Film with id " + film.getId() + " not found in the collection.");
            throw new ValidationExceptionUser("The list Films is empty or not not this element");

        } else {
            films.put(film.getId(), film);
            log.info("Film {} updated with id: {}", film, film.getId());
        }
        return film;
    }

    @Override
    public void delete(int id) {

    }

}

