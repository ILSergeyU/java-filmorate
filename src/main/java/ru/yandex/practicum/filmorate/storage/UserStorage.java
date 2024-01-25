package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    List<User> findAllUsers();

    User findUserById(int id);

    User createUser(User user);

    User updateUsers(User user);

    public Map<Integer, User> getUsers();

}

