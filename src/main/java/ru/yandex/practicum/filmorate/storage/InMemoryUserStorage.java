package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.IncorrectNumberException;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionUser;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Data
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(int id) {
        if (users.containsKey(id) == true) {
            log.info("Пользователь найден: {} ", users.get(id));
            return users.get(id);
        } else
            log.error("Пользователя c Id: {} нет!", id);
        throw new IncorrectNumberException("Пользователя c введенным Id нет");
    }

    @Override
    public User createUser(User user) {
        try {
            user.setId(users.size() + 1);
            log.info("User created: {}", user);
            users.put(users.size() + 1, user);
        } catch (ValidationExceptionUser e) {
            log.error("User creation error: {}", e.getMessage());
        }
        return user;
    }

    @Override
    public User updateUsers(User user) {
        if (users.isEmpty() || users.get(user.getId()) == null) {
            throw new ValidationExceptionUser("The list Films is empty or not this element");
        } else {
            users.put(user.getId(), user);
            log.info("User {} updated with id: {}", user, user.getId());
        }
        return user;
    }
}

