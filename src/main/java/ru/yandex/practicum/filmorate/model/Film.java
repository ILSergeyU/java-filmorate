package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.anotaciy.ReleaseDateAfter1895;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film implements Comparable<Film> {


    Set<Integer> likeFilm;
    private int id;
    @NotBlank(message = "The name of the film dosen't be is empty")
    private String name;
    @Size(max = 200, message = "The maximum length is  200 characters")
    private String description;
    @ReleaseDateAfter1895(message = "The release date film earlier 28 december 1985")
    private LocalDate releaseDate;
    @Positive(message = "The film duration  must be positive")
    private int duration;
    private int summLike = likeFilm.size();

    public void addLike(Integer id) {
        likeFilm.add(id);
    }

    public void deleteLike(Integer id) {
        likeFilm.remove(id);
    }

//    public Integer summLike(){
//        return likeFilm.size();
//    }

    @Override
    public int compareTo(Film like) {
        if (this.summLike == like.summLike) {
            return 0;
        } else if (this.id < like.summLike) {
            return -1;

        } else {
            return 1;
        }
    }
}
