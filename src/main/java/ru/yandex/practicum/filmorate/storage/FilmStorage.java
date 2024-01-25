package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;


public interface FilmStorage {

    List<Film> findAll();

    Film findFilmById(int id);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    public Map<Integer, Film> getFilms();

}
