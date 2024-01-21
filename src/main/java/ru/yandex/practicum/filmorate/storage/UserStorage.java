package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {

    List<User> userAll();

    User userById(int id);

    User createUser(User user);

    User updateUsers(User user);

}

