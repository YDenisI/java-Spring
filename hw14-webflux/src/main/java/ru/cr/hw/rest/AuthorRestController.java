package ru.cr.hw.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.rest.exceptions.NotFoundException;
import ru.cr.hw.services.AuthorService;



@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public Flux<AuthorDto> getAllAuthors() {
        return authorService.findAll()
                .onErrorResume(NotFoundException.class, e -> Flux.empty());
    }
}
