package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    @NotBlank(message = "The name of the film dosen't be is empty")
    private String name;
    @Size(max = 200, message = "The maximum length is  200 characters")
    private String description;
    private LocalDate releaseDate;

    private int duration;

    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new ValidationException("The film Duration  must be positive");

        }
        this.duration = duration;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        LocalDate localDate = LocalDate.of(1895, 12, 28);
        if (releaseDate == null || releaseDate.isBefore(localDate)) {
            throw new ValidationException("The release date film earlier 28 december 1985");
        }
        this.releaseDate = releaseDate;
    }

}
