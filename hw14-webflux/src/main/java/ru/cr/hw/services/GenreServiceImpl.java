package ru.cr.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.repostory.GenreRepository;
import ru.cr.hw.rest.exceptions.NotFoundException;


@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Flux<GenreDto> findAll() {
        return genreRepository.findAll()
                .map(GenreDto::fromDomain);
    }

    @Override
    public Mono<GenreDto> findById(String id) {
        return genreRepository.findById(id)
                .map(GenreDto::fromDomain);
    }
}
