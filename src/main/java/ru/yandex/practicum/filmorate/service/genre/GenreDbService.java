package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.dao.genre.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.dao.genre.GenreDao;

import java.util.Collection;

import static ru.yandex.practicum.filmorate.exception.dao.genre.GenreNotFoundException.GENRE_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class GenreDbService implements GenreService {
    private final GenreDao genreDao;

    @Override
    public Genre get(int genreID) {
        if (genreID > genreID) {
            throw new IllegalArgumentException("genreID должен быть типа INTEGER");
        }
        if (!genreDao.contains(genreID)) {
            log.warn("Не удалось вернуть жанр: {}.", String.format(GENRE_NOT_FOUND, genreID));
            throw new GenreNotFoundException(String.format(GENRE_NOT_FOUND, genreID));
        }
        return genreDao.get(genreID);
    }

    @Override
    public Collection<Genre> getAll() {
        return genreDao.getAll();
    }
}