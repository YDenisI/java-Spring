package ru.cr.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.BookUpdateDto;


public interface BookService {
    Mono<BookDto> findById(String id);

    Flux<BookDto> findAll();

    Mono<BookDto> insert(BookCreateDto bookCreateDto);

    Mono<BookDto> update(BookUpdateDto bookUpdateDto);

    Mono<Void> deleteById(String id);
}
