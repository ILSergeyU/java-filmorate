package ru.yandex.practicum.filmorate.storage.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public interface FilmStorage {
    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> findAll();

    Film findFilmById(int id);

    void addGenres(int filmID, Set<Genre> genres);

    void updateGenres(int filmID, Set<Genre> genres);

    Set<Genre> getGenres(int filmID);

    void delete(int id);

    boolean contains(int filmID);

    Film get(int filmID);

    Collection<Film> getAll();

    Film update(Film film);

}


