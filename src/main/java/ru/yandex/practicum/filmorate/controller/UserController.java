package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class UserController {
    private List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> userAll() {
        return users;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        user.setId(users.size() + 1);
        user.setName(user.getName());
        users.add(user);
        return user;

    }

    @PutMapping("/users")
    public User newUsers(@Valid @RequestBody User user) {
        if (users.isEmpty() || users.get(user.getId() - 1) == null) {
            throw new ValidationException("The list Films is empty or not this element");
        } else {
            users.set(user.getId() - 1, user);
        }
        return user;
    }

}
