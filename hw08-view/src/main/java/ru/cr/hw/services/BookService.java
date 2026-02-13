package ru.cr.hw.services;

import ru.cr.hw.dto.BookDto;
import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, long genreId);

    BookDto update(long id, String title, long authorId, long genreId);

    void deleteById(long id);
}
