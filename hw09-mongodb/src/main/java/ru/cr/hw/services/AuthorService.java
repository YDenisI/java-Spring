package ru.cr.hw.services;

import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;

import java.util.List;

public interface AuthorService {

    List<Author> findAll();

    Author insert(Author author);

    Author update(String id, String fullName);
}
