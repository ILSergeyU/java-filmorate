package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IncorrectNumberException;
import ru.yandex.practicum.filmorate.exception.ValidationExceptionUser;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return userStorage.findAllUsers();
    }

    public User findUserById(int id) {
        return userStorage.findUserById(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUsers(User user) {
        return userStorage.updateUsers(user);
    }

    public User addFriends(Integer id, Integer friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new ValidationExceptionUser("ID отрицательное или равно 0");
        }
        if (id == null || friendId == null) {
            throw new ValidationExceptionUser("ID равно null");
        }
        if (findId(id) == true) {
            User firstFriend = getUser(id);
            User secondFriend = getUser(friendId);
            firstFriend.addFriendInFriends(friendId);
            secondFriend.addFriendInFriends(id);

            return userStorage.getUsers().get(id);

        } else return userStorage.getUsers().get(id);

    }

    public User deleteFriengs(Integer id, Integer friendId) {

        if (findId(id) == false) {
            User firstFriend = getUser(id);
            User secondFriend = getUser(friendId);
            firstFriend.deleteFriendWithFriends(id);
            secondFriend.deleteFriendWithFriends(friendId);

            return userStorage.getUsers().get(id);

        } else return userStorage.getUsers().get(id);

    }

    public List<User> seeCommonFriends(Integer id, Integer friendId) {
        if (id <= 0 || friendId <= 0) {
            throw new IncorrectNumberException("Присланные значения меньши или равны: 0");
        }

        if (id == null || friendId == null) {
            throw new IncorrectNumberException("Переданные пустые значения");
        }

        if (!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(friendId)) {
            throw new IncorrectNumberException("Таких пользователей нет");
        }

        User firstFriend = getUser(id);
        User secondFriend = getUser(friendId);
        List<User> commonFriends = new ArrayList<>();
        for (Integer firstIdFriend : firstFriend.getFriends()) {
            for (Integer secondIdFriend : secondFriend.getFriends()) {
                if (firstIdFriend == secondIdFriend) {
                    commonFriends.add(userStorage.getUsers().get(firstIdFriend));
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

    public List<User> getListOfFriends(Integer id) {
        List<User> hereFriends = new ArrayList<>();
        log.info("Id пользователя: {} ", id);

        for (Integer idUsers : userStorage.getUsers().get(id).getFriends()) {
            hereFriends.add(userStorage.getUsers().get(idUsers));
        }

        return hereFriends;
    }

    public User getUser(Integer id) {
        return userStorage.getUsers().get(id);
    }

}
