package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {


    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findFilmById(int id) {
        return filmStorage.findFilmById(id);
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Integer addingLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilms().get(filmId);
        User user = userStorage.getUsers().get(userId);

        film.addLike(user.getId());
        return film.getLikeFilm().size();
    }

    public Integer removeLike(Integer filmId, Integer userId) {
        if (filmId <= 0 || userId <= 0) {
            throw new IncorrectCountException("Не может быть меньше или равно 0");
        }
        if (filmId == null || userId == null) {
            throw new IncorrectCountException("Не может быть пустым");
        }
        Film film = filmStorage.getFilms().get(filmId);
        User user = userStorage.getUsers().get(userId);

        film.deleteLike(user.getId());
        return film.getLikeFilm().size();
    }

    public Collection<Film> topFilms(Integer count) {
        return filmStorage.getFilms().values().stream()
                .sorted((film1, film2) -> film2.getLikeFilm().size() - film1.getLikeFilm().size())
                .limit(count)
                .collect(Collectors.toList());
    }


}
