package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addFriends(Integer id, Integer friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new ValidationException("ID отрицательное или равно 0");
        }
        if (id == null || friendId == null) {
            throw new ValidationException("ID равно null");
        }
        if (findId(id) == true) {
            User firstFriend = getUser(id);
            User secondFriend = getUser(friendId);
            firstFriend.addFriendInFriends(friendId);
            secondFriend.addFriendInFriends(id);

            return inMemoryUserStorage.getUsers().get(id);

        } else return inMemoryUserStorage.getUsers().get(id);

    }

    public User deleteFriengs(Integer id, Integer friendId) {
        if (findId(id) == false) {
            User firstFriend = getUser(id);
            User secondFriend = getUser(friendId);
            firstFriend.deleteFriendWithFriends(id);
            secondFriend.deleteFriendWithFriends(friendId);

            return inMemoryUserStorage.getUsers().get(id);

        } else return inMemoryUserStorage.getUsers().get(id);

    }

    public List<User> seeCommonFriends(Integer id, Integer friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new IncorrectCountException("Присланные значения меньши или равны: 0");
        }

        if (id == null || friendId == null) {
            throw new IncorrectCountException("Переданные пустые значения");
        }

        if (!inMemoryUserStorage.getUsers().containsKey(id) || !inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new IncorrectCountException("Таких пользователей нет");
        }

        User firstFriend = getUser(id);
        User secondFriend = getUser(friendId);
        List<User> commonFriends = new ArrayList<>();
        for (Integer firstIdFriend : firstFriend.getFriends()) {
            for (Integer secondIdFriend : secondFriend.getFriends()) {
                if (firstIdFriend == secondIdFriend) {
                    commonFriends.add(inMemoryUserStorage.getUsers().get(firstIdFriend));
                }
            }
        }
        return commonFriends;

    }

    public Boolean findId(Integer id) {
        boolean notFriend = true;
        User firstFriend = getUser(id);

        for (Integer friend : firstFriend.getFriends()) {
            if (friend == id) {
                notFriend = false;
                break;
            }
        }
        return notFriend;
    }

    public List<User> hereFriend(Integer id) {
        List<User> hereFriends = new ArrayList<>();
        log.info("Id пользователя: {} ", id);

        for (Integer idUsers : inMemoryUserStorage.getUsers().get(id).getFriends()) {
            hereFriends.add(inMemoryUserStorage.getUsers().get(idUsers));
        }

        return hereFriends;
    }

    public User getUser(Integer id) {
        return inMemoryUserStorage.getUsers().get(id);
    }

}
