package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage<T> {

    T add(T element);


    T update(T element);


    T get(int elementID);


    Collection<T> getAll();


    boolean contains(int elementID);
}