package ru.yandex.practicum.filmorate.storage.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendsMapper implements RowMapper<Friends> {

    @Override
    public Friends mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Friends friends = new Friends();

        friends.setFromUserId(resultSet.getInt("friend_1"));
        friends.setToUserId(resultSet.getInt("friend_2"));
        friends.setIsMutual(resultSet.getBoolean("cofirmation"));

        return friends;
    }
}
