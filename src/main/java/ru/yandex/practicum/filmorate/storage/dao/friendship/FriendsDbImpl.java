package ru.yandex.practicum.filmorate.storage.dao.friendship;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.mapper.FriendsMapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Repository
@Primary
public class FriendsDbImpl implements FriendsStorage {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public FriendsDbImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }


    @Override
    public void add(int fromUserID, int toUserID, boolean isMutual) {
        log.debug("add({}, {}, {}).", fromUserID, toUserID, isMutual);
        jdbcTemplate.update(""
                + "INSERT INTO frinds (friend_1, friend_2, cofirmation) "
                + "VALUES(?, ?, ?)", fromUserID, toUserID, isMutual);
        Friends result = get(fromUserID, toUserID);
        log.trace("Добавлена связь: {}.", result);
    }

    @Override
    public void delete(int fromUserID, int toUserID) {
        log.info("Зашли в метод делит ");
        log.debug("delete({}, {}).", fromUserID, toUserID);
        Friends result = Objects.requireNonNull(get(fromUserID, toUserID));
        jdbcTemplate.update(""
                + "DELETE FROM frinds "
                + "WHERE friend_1=? "
                + "AND friend_2=?", fromUserID, toUserID);
        if (result.getIsMutual()) {
            jdbcTemplate.update(""
                    + "UPDATE frinds "
                    + "SET cofirmation=false "
                    + "WHERE friend_1=? "
                    + "AND friend_2=?", toUserID, fromUserID);
            log.debug("Дружба между {} и {} перестала быть взаимной.",
                    toUserID, fromUserID);
        }
        log.trace("Удалена связь: {}.", result);
    }

    @Override
    public Collection<Integer> getFromUserIDs(int toUserId) {
        log.debug("getFriendships({}).", toUserId);
        List<Integer> friendships = jdbcTemplate.query(format(""
                        + "SELECT friend_1, friend_2, cofirmation "
                        + "FROM frinds "
                        + "WHERE friend_2=%d", toUserId), new FriendsMapper())
                .stream()
                .map(Friends::getFromUserId)
                .collect(Collectors.toList());
        log.trace("Возвращены запросы на дружбу с пользователем ID_{}: {}.",
                toUserId, friendships);
        return friendships;
    }

    @Override
    public boolean contains(int fromUserID, int toUserID) {
        log.debug("contains({}, {}).", fromUserID, toUserID);
        try {
            get(fromUserID, toUserID);
            log.trace("Найден запрос на дружбу от пользователя ID_{} к пользователю ID_{}.",
                    fromUserID, toUserID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Запрос на дружбу от пользователя ID_{} к пользователю ID_{} не найден.",
                    fromUserID, toUserID);
            return false;
        }
    }

    @Override
    public Friends get(int fromUserID, int toUserID) {
        return jdbcTemplate.queryForObject(format(""
                + "SELECT friend_1, friend_2, cofirmation "
                + "FROM frinds "
                + "WHERE friend_1=%d "
                + "AND friend_2=%d", fromUserID, toUserID), new FriendsMapper());
    }

}
