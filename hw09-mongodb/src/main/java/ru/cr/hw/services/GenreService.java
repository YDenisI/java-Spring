package ru.cr.hw.services;

import ru.cr.hw.models.Author;
import ru.cr.hw.models.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();

    Genre insert(Genre genre);

    Genre update(String id, String name);
}
