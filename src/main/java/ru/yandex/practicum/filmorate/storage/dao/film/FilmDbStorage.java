package ru.yandex.practicum.filmorate.storage.dao.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

import static java.lang.String.format;
import static ru.yandex.practicum.filmorate.exception.film.FilmNotFoundException.FILM_NOT_FOUND;

@Slf4j
@Repository
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film createFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO film (film_name, description, releaseDate, duration, rating_id) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());

            return ps;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        jdbcTemplate.update(
                "UPDATE film SET film_name=?, description=?, duration=?, releaseDate=?, rating_id=? WHERE film_id=?",
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                Date.valueOf(film.getReleaseDate()),
                film.getMpa().getId(),
                film.getId()
        );

        return film;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(
                "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.rating_id", (resultSet, rowNum) -> FilmMapper.mapFilm(resultSet));
    }

    public Film findFilmById(int id) {
        if (!contains(id)) {
            log.warn("Не удалось вернуть фильм: {}.", String.format(FILM_NOT_FOUND, id));
            throw new FilmNotFoundException(String.format(FILM_NOT_FOUND, id));
        }

        return jdbcTemplate.queryForObject(
                "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.rating_id " +
                        "WHERE f.film_id = ?", (resultSet, rowNum) -> {
                    Mpa mpa = new Mpa(
                            resultSet.getInt("rating_id"),
                            resultSet.getString("rating_name")
                    );

                    Film film = new Film(
                            resultSet.getInt("film_id"),
                            resultSet.getString("film_name"),
                            resultSet.getString("description"),
                            resultSet.getInt("duration"),
                            resultSet.getDate("releaseDate").toLocalDate(),
                            mpa
                    );

                    film.setGenres(getGenres(id)); // Используем метод getGenres вместо filmStorage.getGenres
                    return film;
                }, id);
    }


    @Override
    public void addGenres(int filmID, Set<Genre> genres) {
        log.debug("addGenres({}, {})", filmID, genres);

        List<Object[]> batchArgs = new ArrayList<>();
        for (Genre genre : genres) {
            batchArgs.add(new Object[]{filmID, genre.getId()});
            log.trace("Genre ID_{} added to film ID_{}.", genre.getId(), filmID);
        }

        jdbcTemplate.batchUpdate(
                "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)",
                batchArgs
        );
    }

    @Override
    public void updateGenres(int filmID, Set<Genre> genres) {
        log.debug("updateGenres({}, {})", filmID, genres);
        deleteGenres(filmID);
        addGenres(filmID, genres);
    }

    @Override
    public Set<Genre> getGenres(int filmID) {
        log.debug("getGenres({})", filmID);
        return new HashSet<>(jdbcTemplate.query(format(""
                + "SELECT f.genre_id, g.name "
                + "FROM film_genre AS f "
                + "LEFT OUTER JOIN genre AS g ON f.genre_id = g.genre_id "
                + "WHERE f.film_id=%d "
                + "ORDER BY g.genre_id", filmID), new GenreMapper()));
    }

    @Override
    public void delete(int id) {
        log.debug("delete({})", id);
        deleteGenres(id);
    }

    @Override
    public boolean contains(int filmID) {
        log.debug("contains({})", filmID);
        try {
            get(filmID);
            log.trace("Film ID_{} exists.", filmID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Film ID_{} does not exist.", filmID);
            return false;
        }
    }

    @Override
    public Film get(int filmID) {
        log.debug("get({})", filmID);
        return jdbcTemplate.queryForObject(format(""
                + "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name "
                + "FROM film AS f "
                + "INNER JOIN mpa AS m ON f.rating_id = m.rating_id "
                + "WHERE f.film_id=%d", filmID), (resultSet, rowNum) -> FilmMapper.mapFilm(resultSet));
    }

    @Override
    public Collection<Film> getAll() {
        log.info("Getting all films");
        List<Film> films = jdbcTemplate.query(
                "SELECT f.film_id, f.film_name, f.description, f.duration, f.releaseDate, f.rating_id, m.rating_name " +
                        "FROM film AS f " +
                        "INNER JOIN mpa AS m ON f.rating_id = m.rating_id", (resultSet, rowNum) -> FilmMapper.mapFilm(resultSet));
        log.trace("Fetched all films: {}.", films);
        log.info("Fetched all films: {}.", films);
        return films;
    }

    @Override
    public Film update(Film film) {
        log.debug("update({})", film);
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
        return get(film.getId());
    }

    private void deleteGenres(int filmID) {
        log.debug("deleteGenres({})", filmID);
        jdbcTemplate.update(""
                + "DELETE "
                + "FROM film_genre "
                + "WHERE film_id=?", filmID);
        log.debug("All genres deleted for film {}.", filmID);
    }
}