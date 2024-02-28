package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.annotation.ReleaseDateAfter1895;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Film.
 */

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {

    private int id;

    @NotBlank(message = "The name of the film dosen't be is empty") // Название фильма не должно быть пустым
    private String name;

    @Size(max = 200, message = "The maximum length is  200 characters")
    private String description;

    @ReleaseDateAfter1895(message = "The release date film earlier 28 december 1985")
    private LocalDate releaseDate;

    @NotNull(message = "У фильма должна быть указана продолжительность")
    @Positive(message = "The film duration  must be positive")
    private Integer duration;
    Set<Integer> likeFilm = new TreeSet<>();
    private int summLike = likeFilm.size();

    private Mpa mpa;

    private Set<Genre> genres = new HashSet<>();


    // Конструктор с параметрами
    public Film(int id, String name, String description, int duration, LocalDate releaseDate, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
    }
}
