package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;


@Slf4j
@Data
public class User {
    private int id = 1;
    @Email(message = "The email don't be empty and must contain char @")
    private String email;
    @Pattern(regexp = "\\S+", message = "The login must not be empty and contains a space character")
    @NotBlank(message = "The login must not be empty and contains a space character")
    private String login;

    private String name;

    @Past(message = "The birthday must not be in the future")
    private LocalDate birthday;

    private Set<Integer> friends = new TreeSet<>();

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            this.name = this.login;
        } else {
            this.name = name;
        }
    }

    public void addFriendInFriends(Integer id) {
        friends.add(id);
    }

    public void deleteFriendWithFriends(Integer id) {
        friends.remove(id);
    }

}
