package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> userAll();

    User userById(int id);

    User createUser(User user);

    User updateUsers(User user);

}

