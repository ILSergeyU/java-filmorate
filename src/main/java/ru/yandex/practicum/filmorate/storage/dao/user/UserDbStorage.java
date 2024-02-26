package ru.yandex.practicum.filmorate.storage.dao.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.util.List;
import java.util.Map;

import static java.lang.String.format;

@Repository
@Primary
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;


    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public User createUser(User user) {
        jdbcTemplate.update("INSERT INTO users (email, login, name, birthday)" +
                        " VALUES (?, ?, ?, ?)",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                (user.getBirthday()));

        return jdbcTemplate.queryForObject(format("" +
                        "SELECT * " +
                        "FROM  users " +
                        "WHERE email = '%s' " +
                        "AND login = '%s' " +
                        "AND name = '%s' ",
                user.getEmail(),
                user.getLogin(),
                user.getName()
        ), new UserMapper());
    }

    @Override
    public User updateUsers(User user) {
        jdbcTemplate.update("" +
                        "UPDATE users SET email=?, login=?, name=?, birthday=? " +
                        "WHERE user_id = ?",
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return jdbcTemplate.queryForObject(format("" +
                        "SELECT * " +
                        "FROM users " +
                        "WHERE user_id = %d",
                user.getId()), new UserMapper());
    }

    @Override
    public User findUserById(int id) {


        return jdbcTemplate.queryForObject(format("" +
                        "SELECT * " +
                        "FROM users " +
                        "WHERE user_id = %d",
                id), new UserMapper());


    }

    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.query("" +
                "SELECT * FROM users", new UserMapper());
    }


    @Override
    public Map<Integer, User> getUsers() {
        return null;
    }

    @Override
    public User get(int userID) {
        log.debug("get({}).", userID);
        User user = jdbcTemplate.queryForObject(format(""
                + "SELECT user_id, email, login, name, birthday "
                + "FROM users "
                + "WHERE user_id=%d", userID), new UserMapper());
        log.trace("Возвращён пользователь: {}", user);
        return user;
    }

    public boolean contains(int userID) {
        log.debug("contains({}).", userID);
        try {
            get(userID);
            log.trace("Найден пользователь ID_{}.", userID);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.trace("Не найден пользователь ID_{}.", userID);
            return false;
        }
    }
}
