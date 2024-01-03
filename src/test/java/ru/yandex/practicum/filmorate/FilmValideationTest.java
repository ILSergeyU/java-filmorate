package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmValideationTest {
    Film film = new Film();

    @Test
    void testFilmName() {
        String nameFilm = "Avengers";

        film.setName(nameFilm);
        assertEquals(nameFilm, film.getName());

        try {
            film.setName("");
        } catch (ValidationException exception) {
            assertEquals("The name of the film dosen't be is empty", exception.getMessage());
        }
    }

    @Test
    void testRelesaseDescription() {
        String descriptionFilm = "Film good";

        film.setDescription(descriptionFilm);
        assertEquals(descriptionFilm, film.getDescription());

        try {
            film.setDescription("11111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                    "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111" +
                    "1111111111111111111");
        } catch (ValidationException exception) {
            assertEquals("The maximum length is  200 characters", exception.getMessage());
        }
    }

    @Test
    void testRealeseDate() {
        LocalDate filmDate = LocalDate.of(1891, 11, 12);

        try {
            film.setReleaseDate(filmDate);
        } catch (ValidationException exception) {
            assertEquals("The release date film earlier 28 december 1985", exception.getMessage());
        }


    }

    @Test
    void testDuration() {
        Duration duration = Duration.ofNanos(-1000);
        System.out.println(duration);
        try {
            film.setDuration(duration);
        } catch (ValidationException exception) {
            assertEquals("The film Duration  must be positive", exception.getMessage());
        }
    }
}
