package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FilmService {
    // Добавление лайка
// Удаление лайка
// Вывод 10 наиболее популярных фильмов по количеству лайков.
// Каждый пользователь может поставить лайк фильму только один раз
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

    public List<Film> seeLiceFilmsTen(Integer films) {
        inMemoryFilmStorage.getFilms().values();
        List<Film> likeFilms = new ArrayList<>();
        List<Film> likeFilmsTen = new ArrayList<>();
        for (Film likeFilm : inMemoryFilmStorage.getFilms().values()) {
            likeFilms.add(likeFilm);
        }
        Collections.sort(likeFilms);
        if (films == null) {
            if (likeFilms.size() <= 10) {
                return likeFilms;
            } else {
                for (int i = 0; i < 10; i++) {
                    likeFilmsTen.add(likeFilms.get(i));
                }
            }

            return likeFilmsTen;
        } else if (films != null) {
            if (likeFilms.size() <= films) {
                return likeFilms;
            } else {
                for (int i = 0; i < films; i++) {
                    likeFilmsTen.add(likeFilms.get(i));
                }
            }
            return likeFilmsTen;
        }
        return likeFilms;
    }
}
