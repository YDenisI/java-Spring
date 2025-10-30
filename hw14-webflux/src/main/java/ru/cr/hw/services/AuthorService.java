package ru.cr.hw.services;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.cr.hw.dto.AuthorDto;

public interface AuthorService {

    Flux<AuthorDto> findAll();

    Mono<AuthorDto> findById(String id);
}
