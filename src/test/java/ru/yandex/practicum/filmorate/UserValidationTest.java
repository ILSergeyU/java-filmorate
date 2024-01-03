package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserValidationTest {

    User user = new User();

    @Test
    void testEmailDont() {

        String email = "Lort@mail.ru";
        user.setEmail(email);
        assertEquals(email, user.getEmail());

        try {
            String emailEmpty = "";
            user.setEmail(emailEmpty);

        } catch (ValidationException exception) {
            assertEquals("The email don't be empty and must contain char @", exception.getMessage());
        }

        try {
            String emailNotChar = "Lort@mail.ru";
            user.setEmail(emailNotChar);

        } catch (ValidationException exception) {
            assertEquals("The email don't be empty and must contain char @", exception.getMessage());
        }
    }

    @Test
    void testName(){
            String name = "Barac";

        user.setName(name);
        assertEquals(name, user.getName());
    }

    @Test
    void testLoginNotEmpty() {
        String login = "MotorMan)";
        user.setLogin(login);

        assertEquals(login, user.getLogin());

        try {
            String loginEmpty = "";
            user.setLogin(loginEmpty);

        } catch (ValidationException exception) {
            assertEquals("The login must not be empty and contains a space character", exception.getMessage());
        }

        try {
            String loginWathSpace = "Motor Man)";
            user.setLogin(loginWathSpace);

        } catch (ValidationException exception) {
            assertEquals("The login must not be empty and contains a space character", exception.getMessage());
        }
    }

    @Test
    void testBirthday() {
        LocalDate birthday = LocalDate.of(1986, 12, 01);
        user.setBirthday(birthday);
        assertEquals(birthday, user.getBirthday());

        try {
            LocalDate birthdayFuture = LocalDate.of(2030, 03, 05);
            user.setBirthday(birthdayFuture);

        } catch (ValidationException exception) {
            assertEquals("The birthday must not be in the future", exception.getMessage());
        }

    }
}
