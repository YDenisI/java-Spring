package ru.cr.hw.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.services.AuthorService;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthorRestController {

    private final AuthorService authorService;

    @GetMapping("/api/authors")
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        List<AuthorDto> authors = authorService.findAll();
        return ResponseEntity.ok(authors);
    }
}
