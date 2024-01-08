package ru.yandex.practicum.filmorate.anotaciy;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateAfterValidator implements ConstraintValidator<ReleaseDateAfter1895, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Разрешить пустые значения, если это приемлемо в вашем контексте
        }

        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return value.isAfter(minDate);
    }
}