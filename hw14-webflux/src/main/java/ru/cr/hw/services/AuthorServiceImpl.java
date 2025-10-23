package ru.cr.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.repostory.AuthorRepository;
import ru.cr.hw.rest.exceptions.NotFoundException;


@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public Flux<AuthorDto> findAll() {
        return authorRepository.findAll()
                .map(AuthorDto::fromDomain);
    }

    @Override
    public Mono<AuthorDto> findById(String id) {
        return authorRepository.findById(id)
                .map(AuthorDto::fromDomain);
    }
}
