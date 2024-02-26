package ru.yandex.practicum.filmorate.storage.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User updateUsers(User user);

    User findUserById(int id);

    List<User> findAllUsers();

    boolean contains(int userID);

    User get(int userID);


}

