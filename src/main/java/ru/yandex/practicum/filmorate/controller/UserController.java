package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;


    @GetMapping
    public List<User> findAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")// Работает
    public User findUserById(@PathVariable int id) {
        log.info("Запрос на получение пользователя по Id");
        return userService.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getListOfFriends(@PathVariable Integer id) {
        log.info("Получение списка друзей у пользователя: {} ", id);
        return userService.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> seeCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получение списка общих друзей у пользователей: {}, {}  ", id, otherId);
        return userService.seeCommonFriends(id, otherId);
    }

    @PostMapping// Работает
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создали пользователя: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User user) {
        log.info("Обновили пользователя: {}", user);
        return userService.updateUsers(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователи добавляют друг друга в друзья {} , {}", id, friendId);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriengs(@PathVariable int id, @PathVariable int friendId) {
        log.info("Пользователи удаляют друг друга из друзей {} , {}", id, friendId);
        return userService.deleteFriengs(id, friendId);
    }

}