package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@RestController
public class UserController {
    private List<User> users = new LinkedList<>();

    @GetMapping("/users")
    public List<User> userAll() {
        return users;
    }

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        try {
            user.setId(users.size() + 1);
            user.setName(user.getName());
            log.info("Пользователь:", user);
            users.add(user);
        } catch (ValidationException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
        }
        return user;

    }

    @PutMapping("/users")
    public User update(@Valid @RequestBody User user) {
        try {
            int userId = user.getId() - 1;
            if (userId >= 0 && userId < users.size()) {
                if (users.isEmpty() || users.get(userId) == null) {
                    throw new ValidationException("The list Users is empty or does not contain this element");
                } else {
                    users.set(userId, user);
                    log.info("Пользователь {} с id:{} обновлен ", user, userId);
                }
            } else {
                throw new ValidationException("Invalid user ID");
            }
        } catch (ValidationException e) {
            log.error("Ошибка валидации: {}", e.getMessage());
        }
        return user;
    }

}
