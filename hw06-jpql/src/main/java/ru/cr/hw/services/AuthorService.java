package ru.cr.hw.services;

import ru.cr.hw.models.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
