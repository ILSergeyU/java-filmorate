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


    //    @PutMapping("/users")
//    public User update(@Valid @RequestBody User user) {
//        try {
//            int index = user.getId() - 1;
//            if (index >= 0 && index < users.size()) {
//                User existingUser = users.get(index);
//                users.set(index, user);
//                log.info("Пользователь {} с id:{} обновлен ", user, index);
//            } else {
//                throw new ValidationException("Invalid user ID");
//            }
//        } catch (ValidationException e) {
//            log.error("Ошибка валидации: {}", e.getMessage());
//        }
//        return user;
//    }
    @PutMapping("/users")
    public User newUsers(@Valid @RequestBody User user) {
        if (users.isEmpty() || users.get(user.getId() - 1) == null) {
            throw new ValidationException("The list Films is empty or not this element");
        } else {
            users.set(user.getId() - 1, user);
            log.info("Пользователь {} с id:{} обновлен ", user, user.getId() - 1);
        }
        return user;
    }
}
