package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidationTest {

    User user = new User();
    private Validator validator;

    @BeforeEach
    void setUser() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void testEmailDont() {
        String email = "Murmansk.axsel@yandex.ru";
        user.setEmail(email);
// Почему идет проверка так же и логина
        String login = "MotorMan)";
        user.setLogin(login);

        assertEquals(email, user.getEmail());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());

        String emailEmpty = "";
        user.setEmail(emailEmpty);
        String emailNotChar = "Lortmail.ru";
        user.setEmail(emailNotChar);
        Set<ConstraintViolation<User>> violationsEmail = validator.validate(user);
        boolean foundErrorMessage = false;
        for (ConstraintViolation<User> violation : violationsEmail) {
            if ("The email don't be empty and must contain char @".equals(violation.getMessage())) {

                foundErrorMessage = true;
                break;
            }
        }
        assertTrue(foundErrorMessage);


    }

    @Test
    void testName() {
        String name = "Barac";
        user.setName(name);
        assertEquals(name, user.getName());
    }

    @Test
    void testLoginNotEmpty() {
        String login = "MotorMan)";
        user.setLogin(login);

        assertEquals(login, user.getLogin());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());

        String loginEmpty = null;
        user.setLogin(loginEmpty);

        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        boolean foundErrorMessage = false;
        for (ConstraintViolation<User> violation : violationSet) {
            if ("The login must not be empty and contains a space character".equals(violation.getMessage())) {

                foundErrorMessage = true;
                break;
            }
        }
        assertTrue(foundErrorMessage);


    }

    @Test
    void testBirthday() {
        LocalDate birthday = LocalDate.of(1986, 12, 01);
        user.setBirthday(birthday);
        String login = "MotorMan)";
        user.setLogin(login);
        String name = "Barac";
        user.setName(name);
        assertEquals(birthday, user.getBirthday());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
        LocalDate nowDate = LocalDate.now();
        LocalDate birsdayBad = nowDate.plusYears(5);
        user.setBirthday(birsdayBad);

        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        boolean foundErrorMessage = false;
        for (ConstraintViolation<User> violation : violationSet) {
            if ("The birthday must not be in the future".equals(violation.getMessage())) {
                foundErrorMessage = true;
                break;
            }
        }
        assertTrue(foundErrorMessage);

    }

}
