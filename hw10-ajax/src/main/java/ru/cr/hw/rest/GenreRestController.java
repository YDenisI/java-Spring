package ru.cr.hw.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.services.GenreService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping("/api/genres")
    public ResponseEntity<List<GenreDto>> getAllGenres() {
        List<GenreDto> genres = genreService.findAll();
        return ResponseEntity.ok(genres);
    }
}
