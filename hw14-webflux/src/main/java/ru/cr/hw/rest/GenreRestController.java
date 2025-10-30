package ru.cr.hw.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.rest.exceptions.NotFoundException;
import ru.cr.hw.services.GenreService;



@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public Flux<GenreDto> getAllGenres() {
        return genreService.findAll()
                .onErrorResume(NotFoundException.class, e -> Flux.empty());
    }
}
