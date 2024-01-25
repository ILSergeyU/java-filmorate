package ru.yandex.practicum.filmorate.exception;

import javax.swing.*;

public class ErrorResponse {
    //Название ошибки
    String error;
    //Описание ошибки
    Spring description;

    public ErrorResponse(String error, Spring description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public Spring getDescription() {
        return description;
    }
}
