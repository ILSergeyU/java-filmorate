package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendshipAlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.IncorrectNumberException;
import ru.yandex.practicum.filmorate.exception.dao.friends.FriendshipNotFoundException;
import ru.yandex.practicum.filmorate.exception.user.UserLogicException;
import ru.yandex.practicum.filmorate.exception.user.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.friendship.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.dao.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.exception.FriendshipAlreadyExistsException.FRIENDSHIP_ALREADY_EXIST;
import static ru.yandex.practicum.filmorate.exception.dao.friends.FriendshipNotFoundException.FRIENDSHIP_NOT_FOUND;
import static ru.yandex.practicum.filmorate.exception.user.UserLogicException.*;
import static ru.yandex.practicum.filmorate.exception.user.UserNotFoundException.USER_NOT_FOUND;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendsStorage friendsStorage) {
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
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


    public void addFriend(int userID, int friendID) {
        checkFriendToAdd(userID, friendID);
        boolean isMutual = friendsStorage.contains(friendID, userID);
        friendsStorage.add(friendID, userID, isMutual);
    }


    public void deleteFriend(Integer userID, Integer friendID) {
        log.info("Зашли в метод Delete");
        checkFriendToDelete(userID, friendID);
        friendsStorage.delete(friendID, userID);
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
        if (!userStorage.contains(id)) {
            log.warn("Не удалось вернуть друзей: {}.", String.format(USER_NOT_FOUND, id));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, id));
        }
        List<User> friends = friendsStorage.getFromUserIDs(id).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::get)
                .collect(Collectors.toList());
        log.trace("Возвращён список друзей: {}.", friends);
        return friends;
    }


    public User getUser(Integer id) {
        return userStorage.getUsers().get(id);
    }


    public Collection<User> getCommonFriends(int userID, int otherUserID) {
        checkCommonFriendToGet(userID, otherUserID);
        List<User> commonFriends = CollectionUtils.intersection(
                        friendsStorage.getFromUserIDs(userID),
                        friendsStorage.getFromUserIDs(otherUserID)).stream()
                .mapToInt(Integer::valueOf)
                .mapToObj(userStorage::get)
                .collect(Collectors.toList());
        log.trace("Возвращён список общих друзей: {}.", commonFriends);
        return commonFriends;
    }

    private void checkFriendToAdd(int userID, int friendID) {
        log.debug("checkFriendToAdd({}, {}).", userID, friendID);
        String msg = "Не удалось добавить друга: {}.";
        if (!userStorage.contains(userID)) {
            log.warn(msg, String.format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userID));
        }
        if (!userStorage.contains(friendID)) {
            log.warn(msg, String.format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, friendID));
        }
        if (userID == friendID) {
            log.warn(msg, String.format(UNABLE_TO_ADD_YOURSELF, userID));
            throw new UserLogicException(String.format(UNABLE_TO_ADD_YOURSELF, userID));
        }
        if (friendsStorage.contains(friendID, userID)) {
            log.warn(msg, String.format(FRIENDSHIP_ALREADY_EXIST, friendID, userID));
            throw new FriendshipAlreadyExistsException(String.format(FRIENDSHIP_ALREADY_EXIST, friendID, userID));
        }
    }

    private void checkCommonFriendToGet(int userID, int otherUserID) {
        if (!userStorage.contains(userID)) {
            log.warn("Не удалось вернуть общих друзей: {}.", String.format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userID));
        }
        if (!userStorage.contains(otherUserID)) {
            log.warn("Не удалось вернуть общих друзей: {}.", String.format(USER_NOT_FOUND, otherUserID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, otherUserID));
        }
        if (userID == otherUserID) {
            log.warn("Не удалось вернуть общих друзей: {}.", String.format(UNABLE_FRIENDS_AMONG_THEMSELVES, userID));
            throw new UserLogicException(String.format(UNABLE_FRIENDS_AMONG_THEMSELVES, userID));
        }
    }

    private void checkFriendToDelete(Integer userID, Integer friendID) {
        log.info("Зашли в метод checkFriendToDelete");
        log.debug("checkFriendToDelete({}, {}).", userID, friendID);
        String msg = "Не удалось удалить друга: {}.";
        if (!userStorage.contains(userID)) {
            log.info("!userStorage.contains(userID)");
            log.warn(msg, String.format(USER_NOT_FOUND, userID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, userID));
        }
        if (!userStorage.contains(friendID)) {
            log.info("!userStorage.contains(friendID)");
            log.warn(msg, String.format(USER_NOT_FOUND, friendID));
            throw new UserNotFoundException(String.format(USER_NOT_FOUND, friendID));
        }
        if (userID == friendID) {
            log.info("userID == friendID");
            log.warn(msg, String.format(UNABLE_TO_DELETE_YOURSELF, userID));
            throw new UserLogicException(String.format(UNABLE_TO_DELETE_YOURSELF, userID));
        }
        if (!friendsStorage.contains(friendID, userID)) {
            log.info("!friendsStorage.contains(friendID, userID)");
            log.warn(msg, String.format(FRIENDSHIP_NOT_FOUND, friendID, userID));
            throw new FriendshipNotFoundException(String.format(FRIENDSHIP_NOT_FOUND, friendID, userID));
        }
    }
}