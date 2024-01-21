package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final InMemoryUserStorage inMemoryUserStorage;


    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Integer addingLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilms().get(filmId);
        User user = inMemoryUserStorage.getUsers().get(userId);

        film.addLike(user.getId());
        return film.getLikeFilm().size();
    }

    public Integer removeLike(Integer filmId, Integer userId) {
        Film film = inMemoryFilmStorage.getFilms().get(filmId);
        User user = inMemoryUserStorage.getUsers().get(userId);

        film.deleteLike(user.getId());
        return film.getLikeFilm().size();
    }

    public Collection<Film> topFilms(Integer count) {
        return inMemoryFilmStorage.getFilms().values().stream()
                .sorted((film1, film2) -> film2.getLikeFilm().size() - film1.getLikeFilm().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
