package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.String.format;

@Slf4j
@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) { // Создание фильма

        jdbcTemplate.update("INSERT INTO film (film_name, description, releaseDate, duration, rating_id)" +
                        " VALUES (?,?,?,?,?)",
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());

        return jdbcTemplate.queryForObject(format(
                "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.rating_id " +
                        "WHERE f.film_name = '%s' "
                        + "AND f.description = '%s' "
                        + "AND f.releaseDate = '%s' "
                        + "AND f.duration = %d "
                        + "AND f.rating_id = %d",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId()), new FilmMapper());

    }

    @Override
    public Film updateFilm(Film film) { // Обновление фильма

        jdbcTemplate.update("UPDATE film SET film_name=?, description =?, duration = ?, releaseDate = ?," +
                        " rating_id =? WHERE film_id = ?",
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                Date.valueOf(film.getReleaseDate()),
                film.getMpa().getId(),
                film.getId());


        return jdbcTemplate.queryForObject(format(
                "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.rating_id " +
                        "WHERE f.film_id = %d ",
                film.getId()), new FilmMapper());

    }

    @Override
    public List<Film> findAll() {
        List<Film> test = jdbcTemplate.query(
                "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.rating_id",
                new FilmMapper()
        );
        log.info("Test: {}", test);
        return test;
    }

    @Override
    public Film findFilmById(int id) { // Запрос на получения фильма по id!!!!!!!!!!!!!!

        return jdbcTemplate.queryForObject(format("" +
                        "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.rating_id " +
                        "WHERE f.film_id = %d ",
                id), new FilmMapper());
    }

    @Override
    public void addGenres(int filmID, Set<Genre> genres) {
        log.debug("add({}, {})", filmID, genres);
        for (Genre genre : genres) {
            jdbcTemplate.update(""
                    + "INSERT INTO film_genre (film_id, genre_id) "
                    + "VALUES (?, ?)", filmID, genre.getId());
            log.trace("Фильму ID_{} добавлен жанр ID_{}.", filmID, genre.getId());
        }
    }

    @Override
    public void updateGenres(int filmID, Set<Genre> genres) {
        log.debug("update({}, {})", filmID, genres);
        delete(filmID);
        addGenres(filmID, genres);
    }

    @Override
    public Set<Genre> getGenres(int filmID) {
        log.debug("getGenres({}).", filmID);
        Set<Genre> genres = new HashSet<>(jdbcTemplate.query(format(""
                + "SELECT f.genre_id, g.name "
                + "FROM film_genre AS f "
                + "LEFT OUTER JOIN genre AS g ON f.genre_id = g.genre_id "
                + "WHERE f.film_id=%d "
                + "ORDER BY g.genre_id", filmID), new GenreMapper()));
        log.trace("Возвращены все жанры для фильма ID_{}: {}.", filmID, genres);
        return genres;
    }

    @Override
    public void delete(int id) {
//        jdbcTemplate.update("DELETE  FROM film WHERE film_id = ?", id);
        log.debug("delete({}).", id);
        jdbcTemplate.update(""
                + "DELETE "
                + "FROM film_genre "
                + "WHERE film_id=?", id);
        log.debug("Удалены все жанры у фильма {}.", id);
    }


    @Override
    public boolean contains(int filmID) {
        log.debug("contains({}).", filmID);
        try {
            get(filmID);
            log.trace("Найден фильм ID_{}.", filmID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Не найден фильм ID_{}.", filmID);
            return false;
        }
    }

    @Override
    public Film get(int filmID) {
        log.debug("get({}).", filmID);
        Film film = jdbcTemplate.queryForObject(format(""
                + "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name "
                + "FROM film AS f "
                + "INNER JOIN mpa AS m ON f.rating_id = m.rating_id "
                + "WHERE f.film_id=%d", filmID), new FilmMapper());
        log.trace("Возвращён фильм: {}", film);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        log.info("getAll().");
        log.debug("getAll().");
        List<Film> films = jdbcTemplate.query(
                "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.rating_id", new FilmMapper());
        log.trace("Возвращены все фильмы: {}.", films);
        log.info("Возвращены все фильмы: {}.", films);
        return films;
    }

    @Override
    public Film update(Film film) {
        log.debug("update({}).", film);
        jdbcTemplate.update(""
                        + "UPDATE film "
                        + "SET film_name=?, description=?, releaseDate=?, duration=?, rating_id=? "
                        + "WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        Film result = get(film.getId());
        log.trace("Обновлён фильм: {}.", result);
        return result;
    }
}



