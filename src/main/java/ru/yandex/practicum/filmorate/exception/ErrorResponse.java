package ru.yandex.practicum.filmorate.exception;

public class ErrorResponse {
    //Название ошибки
    String error;
    //Описание ошибки
    String description;


    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}
