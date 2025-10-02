package ru.cr.hw.services;

import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookDto findById(String id);

    List<BookDto> findAll();

    BookDto insert(BookCreateDto bookCreateDto);

    BookDto update(BookUpdateDto bookUpdateDto);

    void deleteById(String id);
}
