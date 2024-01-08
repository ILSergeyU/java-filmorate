package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> userAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        try {
            user.setId(users.size() + 1);
            user.setName(user.getName());
            log.info("Пользователь:", user);
            users.put(users.size() + 1, user);
        } catch (ValidationException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
        }
        return user;

    }


    @PutMapping
    public User newUsers(@Valid @RequestBody User user) {
        if (users.isEmpty() || users.get(user.getId() /*- 1*/) == null) {
            throw new ValidationException("The list Films is empty or not this element");
        } else {
            users.put(user.getId() /*- 1*/, user);
            log.info("Пользователь {} с id:{} обновлен ", user, user.getId() /*- 1*/);
        }
        return user;
    }
}
