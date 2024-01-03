package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;


@Slf4j
@Data
public class User {
    private int id;
    @Email(message = "The email don't be empty and must contain char @")
    private String email;
    @NotBlank(message = "The login must not be empty and contains a space character" )
    private String login;
    private String name;
    @Past(message = "The birthday must not be in the future")
    private LocalDate birthday;

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            this.name = this.login;
        } else {
            this.name = name;
        }
    }

}
