package ru.cr.hw.services;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.cr.hw.dto.GenreDto;

public interface GenreService {

    Flux<GenreDto> findAll();

    Mono<GenreDto> findById(String id);
}
