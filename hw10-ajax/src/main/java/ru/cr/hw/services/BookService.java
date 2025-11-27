package ru.cr.hw.services;

import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.BookUpdateDto;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    BookDto insert(BookCreateDto createDto);

    BookDto update(BookUpdateDto updateDto);

    void deleteById(long id);
}
