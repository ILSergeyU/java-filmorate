package ru.yandex.practicum.filmorate.anotaciy;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateAfterValidator.class)
public @interface ReleaseDateAfter1895 {
    String message() default "Release date must be after 28 december 1895";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
