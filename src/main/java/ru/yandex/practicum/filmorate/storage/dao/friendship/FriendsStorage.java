package ru.yandex.practicum.filmorate.storage.dao.friendship;

import ru.yandex.practicum.filmorate.model.Friends;

import java.util.Collection;

public interface FriendsStorage {

        void add(int fromUserID, int toUserID, boolean isMutual);

    void delete(int fromUserID, int toUserID);

    Collection<Integer> getFromUserIDs(int toUserId);

    boolean contains(int fromUserID, int toUserID);

    public Friends get(int fromUserID, int toUserID);

}


