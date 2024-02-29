package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.dao.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.dao.mpa.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.FilmAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.film.LikeAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.film.LikeNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.dao.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.dao.genre.GenreNotFoundException.GENRE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.dao.mpa.MpaNotFoundException.MPA_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.film.FilmAlreadyExistsException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException.FILM_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.film.LikeAlreadyExistsException.LIKE_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.exception.film.LikeNotFoundException.LIKE_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.user.UserNotFoundException.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {


    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDao likeDao;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;


    public Film createFilm(Film film) {
        checkFilmToAdd(film);
        Film result = filmStorage.createFilm(film);
        filmStorage.addGenres(result.getId(), film.getGenres());
        result.setGenres(filmStorage.getGenres(result.getId()));
        return result;

    }

    public Film updateFilm(Film film) {
        checkFilmToUpdate(film);
        Film result = filmStorage.update(film);
        filmStorage.updateGenres(result.getId(), film.getGenres());
        result.setGenres(filmStorage.getGenres(result.getId()));
        result.setMpa(mpaDao.get(result.getMpa().getId()));
        return result;
    }

    public Collection<Film> findAll() {
        var films = filmStorage.getAll();
        for (Film film : films) {
            film.setGenres(filmStorage.getGenres(film.getId()));
            film.setMpa(mpaDao.get(film.getMpa().getId()));
        }
        return films;
    }

    public Film findFilmById(int id) {
        if (!filmStorage.contains(id)) {
            log.warn("Не удалось вернуть фильм: {}.", String.format(FILM_NOT_FOUND, id));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, id));
        }
        Film film = filmStorage.get(id);
        film.setGenres(filmStorage.getGenres(id));
        film.setMpa(mpaDao.get(film.getMpa().getId()));
        return film;
    }


    public void addingLike(Integer filmId, Integer userId) {

        likeDao.add(filmId, userId);


    }

    public void removeLike(Integer filmId, Integer userId) {
        checkLikeToDelete(filmId, userId);
        likeDao.delete(filmId, userId);

    }


    private void checkLikeToAdd(int filmID, int userID) {
        log.debug("checkLikeToAdd({}, {}).", filmID, userID);
        String msg = "Не удалось добавить лайк: {}.";
        if (!filmStorage.contains(filmID)) {
            log.warn(msg, String.format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, filmID));
        }
        if (!userStorage.contains(userID)) {
            log.warn(msg, String.format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userID));
        }
        if (likeDao.contains(filmID, userID)) {
            log.warn(msg, String.format(LIKE_ALREADY_EXISTS, filmID, userID));
            throw new LikeAlreadyExistsException(String.format(LIKE_ALREADY_EXISTS, filmID, userID));
        }
    }

    public Collection<Film> getPopularFilms(int count) {
        log.debug("getPopularFilms({}).", count);
        List<Film> popularFilms = filmStorage.findAll().stream()
                .sorted(this::likeCompare)
                .limit(count)
                .collect(Collectors.toList());
        log.trace("Возвращены популярные фильмы: {}.", popularFilms);
        return popularFilms;
    }

    private int likeCompare(Film film, Film otherFilm) {
        return Integer.compare(likeDao.count(otherFilm.getId()), likeDao.count(film.getId()));
    }

    private void checkFilmToAdd(Film film) {
        log.debug("checkFilmToAdd({}).", film);
        String msg = "Не удалось добавить фильм: {}.";
        if (film.getId() != 0) {
            if (filmStorage.contains(film.getId())) {
                log.warn(msg, String.format(FILM_ALREADY_EXISTS, film.getId()));
                throw new FilmAlreadyExistsException(String.format(FILM_ALREADY_EXISTS, film.getId()));
            } else {
                log.warn(msg, "Запрещено устанавливать ID вручную");
                throw new IllegalArgumentException("Запрещено устанавливать ID вручную");
            }
        }
        if (!mpaDao.contains(film.getMpa().getId())) {
            log.warn(msg, String.format(MPA_NOT_FOUND, film.getMpa().getId()));
            throw new MpaNotFoundException(String.format(MPA_NOT_FOUND, film.getMpa().getId()));
        }
        for (Genre genre : film.getGenres()) {
            if (!genreDao.contains(genre.getId())) {
                log.warn(msg, String.format(GENRE_NOT_FOUND, genre.getId()));
                throw new GenreNotFoundException(String.format(GENRE_NOT_FOUND, genre.getId()));
            }
        }
    }

    private void checkLikeToDelete(int filmID, int userID) {
        log.debug("checkLikeToDelete({}, {}).", filmID, userID);
        String msg = "Не удалось удалить лайк: {}.";
        if (!filmStorage.contains(filmID)) {
            log.warn(msg, String.format(FILM_NOT_FOUND, filmID));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, filmID));
        }
        if (!userStorage.contains(userID)) {
            log.warn(msg, String.format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userID));
        }
        if (!likeDao.contains(filmID, userID)) {
            log.warn(msg, String.format(LIKE_NOT_FOUND, filmID, userID));
            throw new LikeNotFoundException(String.format(LIKE_NOT_FOUND, filmID, userID));
        }
    }

    private void checkFilmToUpdate(Film film) {
        log.debug("checkFilmToUpdate({}).", film);
        String msg = "Не удалось обновить фильм: {}.";
        if (!filmStorage.contains(film.getId())) {
            log.warn(msg, String.format(FILM_NOT_FOUND, film.getId()));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, film.getId()));
        }
        if (!mpaDao.contains(film.getMpa().getId())) {
            log.warn(msg, String.format(MPA_NOT_FOUND, film.getMpa().getId()));
            throw new MpaNotFoundException(String.format(MPA_NOT_FOUND, film.getMpa().getId()));
        }
        for (Genre genre : film.getGenres()) {
            if (!genreDao.contains(genre.getId())) {
                log.warn(msg, String.format(GENRE_NOT_FOUND, genre.getId()));
                throw new GenreNotFoundException(String.format(GENRE_NOT_FOUND, genre.getId()));
            }
        }
    }
}
