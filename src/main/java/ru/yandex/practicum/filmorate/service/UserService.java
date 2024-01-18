package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
// Добавление в друзья
// Удаление из друзей
// Вывод списка общих друзей

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addFriends(Integer id, Integer friendId) {//Добавить друга
        if (findId(id) == true) {
            User firstFriend = getUser(id);
            User secondFriend = getUser(friendId);
            firstFriend.addFriendInFriends(id);
            secondFriend.addFriendInFriends(friendId);

            return inMemoryUserStorage.getUsers().get(id);//Этот пользователь добавлен

        } else return inMemoryUserStorage.getUsers().get(id);//Этот пользователь добавлен


    }

    public User deleteFriengs(Integer id, Integer friendId) {//Удалить друга
        if (findId(id) == false) {
            User firstFriend = getUser(id);
            User secondFriend = getUser(friendId);
            firstFriend.deleteFriendWithFriends(id);
            secondFriend.deleteFriendWithFriends(friendId);

            return inMemoryUserStorage.getUsers().get(id);//Этот удалён из друзей

        } else return inMemoryUserStorage.getUsers().get(id);//Этот удалён из друзей

    }

    public List<User> seeCommonFriends(Integer id, Integer friendId) { //Вывод списка общих друзей

        User firstFriend = getUser(id); // inMemoryUserStorage.getUsers().get(id);
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

    public Boolean findId(Integer id) {//Проверка наличия друга
        boolean notFriend = true; // пользователя нет
        User firstFriend = getUser(id);
        //firstFriend.contains(); - попробывать проверку сделать так
        for (Integer friend : firstFriend.getFriends()) {
            if (friend == id) {
                notFriend = false; // пользователь есть
                break;
            }
        }
        return notFriend;
    }

    public List<User> hereFriend(Integer id) {
        List<User> hereFriends = new ArrayList<>();
        for (Integer idUsers : inMemoryUserStorage.getUsers().get(id).getFriends()) {
            hereFriends.add(inMemoryUserStorage.getUsers().get(idUsers));
        }
        return hereFriends;
    }

    public User getUser(Integer id) {
        return inMemoryUserStorage.getUsers().get(id);
    }

}
