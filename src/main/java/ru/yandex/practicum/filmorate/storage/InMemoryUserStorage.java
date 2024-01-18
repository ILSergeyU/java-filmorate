package ru.yandex.practicum.filmorate.storage;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
    public List<User> userAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User userById(int id) {
        return users.get(id);
    }

    @Override
    public User createUser(User user) {
        try {
            user.setId(users.size() + 1);
            user.setName(user.getName());
            log.info("User created: {}", user);
            users.put(users.size() + 1, user);
        } catch (ValidationException e) {
            log.error("User creation error: {}", e.getMessage());
        }
        return user;
    }

    @Override
    public User updateUsers(User user) {
        if (users.isEmpty() || users.get(user.getId()) == null) {
            throw new ValidationException("The list Films is empty or not this element");
        } else {
            users.put(user.getId(), user);
            log.info("User {} updated with id: {}", user, user.getId());
        }
        return user;
    }
}

