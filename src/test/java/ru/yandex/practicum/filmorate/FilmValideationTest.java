package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmValideationTest {
    Film film = new Film();
    private Validator validator;

    @BeforeEach
    void setFilm() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void testFilmName() {
        String nameFilm = "Avengers";

        film.setName(nameFilm);
        assertEquals(nameFilm, film.getName());

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());

        String nameBad = null;
        film.setName(nameBad);
        Set<ConstraintViolation<Film>> violationsBadName = validator.validate(film);
        boolean foundErrorMessage = false;
        for (ConstraintViolation<Film> violation : violationsBadName) {
            if ("The name of the film dosen't be is empty".equals(violation.getMessage())) {
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue(foundErrorMessage);
    }

    @Test
    void testRelesaseDescription() {
        String descriptionGoodFilm = "Film";
        // Понять почему по умолчанию проверяет и поле name!!!
        String nameFilm = "Avengers";
        film.setName(nameFilm);

        film.setDescription(descriptionGoodFilm);
        assertEquals(film.getDescription(), descriptionGoodFilm);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());


        String descriptionBadFilm = "A".repeat(201);
        film.setDescription(descriptionBadFilm);
        Set<ConstraintViolation<Film>> badDescription = validator.validate(film);

        boolean foundErrorMessage = false;

        for (ConstraintViolation<Film> violation : badDescription) {
            if ("The maximum length is  200 characters".equals(violation.getMessage())) {
                System.out.println("Зашол");
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue(foundErrorMessage);

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
        int durationPositiv = 20;
        film.setDuration(durationPositiv);
        assertEquals(durationPositiv, film.getDuration());
        try {
            int duration = -1;
            film.setDuration(duration);
        } catch (ValidationException exception) {
            assertEquals("The film duration  must be positive", exception.getMessage());
        }
    }
}
