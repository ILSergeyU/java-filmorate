package ru.yandex.practicum.filmorate.storage.dao.like;

public interface LikeDao {
    /**
     * Метод добавляет в хранилище лайк (от
     * пользователя фильму).
     *
     * @param filmID идентификатор фильма.
     * @param userID идентификатор пользователя.
     */
    void add(int filmID, int userID);

    /**
     * Метод удаляет лайк (от пользователя
     * фильму) из хранилища.
     *
     * @param filmID идентификатор фильма.
     * @param userID идентификатор пользователя.
     */
    void delete(int filmID, int userID);

    /**
     * Метод подсчитывает количество лайков
     * для фильма с указанным идентификатором.
     *
     * @param filmID идентификатор фильма.
     * @return Количество лайков.
     */
    int count(int filmID);

    /**
     * Метод проверяет наличие лайка
     * (от пользователя фильму) в хранилище.
     *
     * @param filmID идентификатор фильма.
     * @param userID идентификатор пользователя.
     * @return Логическое значение, true - если
     * лайк содержится в хранилище, и false -
     * если нет.
     */
    boolean contains(int filmID, int userID);
}
