package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

        String descriptionGoodFilm = "Film";
        film.setDescription(descriptionGoodFilm);

        int positiveDuration = 90;
        film.setDuration(positiveDuration);

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
        film.setDescription(descriptionGoodFilm);

        // ?????? ?????? ?? ????????? ????????? ? ???? name!!!
        String nameFilm = "Avengers";
        film.setName(nameFilm);

        int positiveDuration = 90;
        film.setDuration(positiveDuration);

        assertEquals(film.getDescription(), descriptionGoodFilm);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());


        String descriptionBadFilm = "A".repeat(201);
        film.setDescription(descriptionBadFilm);
        Set<ConstraintViolation<Film>> badDescription = validator.validate(film);

        boolean foundErrorMessage = false;

        for (ConstraintViolation<Film> violation : badDescription) {
            if ("The maximum length is  200 characters".equals(violation.getMessage())) {
                System.out.println("?????");
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue(foundErrorMessage);

    }


    @Test
    void testReleaseDateAfter1895() {

        String descriptionGoodFilm = "Film";
        // ?????? ?????? ?? ????????? ????????? ? ???? name!!!
        String nameFilm = "Avengers";

        film.setName(nameFilm);
        film.setDescription(descriptionGoodFilm);

        int positiveDuration = 90;
        film.setDuration(positiveDuration);

        // ????????? ???? ?????? ????? 28 ??????? 1895 ????
        LocalDate dateAfter1895 = LocalDate.of(1896, 1, 1);
        film.setReleaseDate(dateAfter1895);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());

        // ????????? ???? ?????? ?? 28 ??????? 1895 ????
        LocalDate dateBefore1895 = LocalDate.of(1895, 12, 27);
        film.setReleaseDate(dateBefore1895);

        Set<ConstraintViolation<Film>> before1895Violations = validator.validate(film);

        boolean foundErrorMessage = false;
        for (ConstraintViolation<Film> violation : before1895Violations) {
            if ("The release date film earlier 28 december 1985".equals(violation.getMessage())) {
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue(foundErrorMessage);
    }

    @Test
    void testPositiveDuration() {
        String descriptionGoodFilm = "Film";
        // ?????? ?????? ?? ????????? ????????? ? ???? name!!!
        String nameFilm = "Avengers";
        film.setName(nameFilm);

        film.setDescription(descriptionGoodFilm);

        // ????????? ????????????? ????????????
        int positiveDuration = 90;
        film.setDuration(positiveDuration);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());

        // ????????? ????????????? ????????????
        int negativeDuration = -100;
        film.setDuration(negativeDuration);

        Set<ConstraintViolation<Film>> negativeViolations = validator.validate(film);

        boolean foundErrorMessage = false;
        for (ConstraintViolation<Film> violation : negativeViolations) {
            if ("The film duration  must be positive".equals(violation.getMessage())) {
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue(foundErrorMessage);
    }
}


