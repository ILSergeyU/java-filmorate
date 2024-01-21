package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserStorage userStorage;
    @Autowired
    private final UserService userService;


    @GetMapping
    public List<User> userAll() {
        return userStorage.userAll();
    }

    @GetMapping("/{id}")// Работает
    public User userById(@PathVariable int id) {

        return userStorage.userById(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> hereFriend(@PathVariable Integer id) {
        return userService.hereFriend(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> seeCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.seeCommonFriends(id, otherId);
    }

    @PostMapping// Работает
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    public User updateUsers(@Valid @RequestBody User user) {
        return userStorage.updateUsers(user);

    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriends(@PathVariable int id, @PathVariable int friendId) {
        return userService.addFriends(id, friendId);

    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriengs(@PathVariable int id, @PathVariable int friendId) {
        return userService.deleteFriengs(id, friendId);
    }


}