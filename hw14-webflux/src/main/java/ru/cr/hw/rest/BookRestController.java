package ru.cr.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookUpdateDto;

import ru.cr.hw.services.BookService;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

   // private final CommentService commentService;

    @GetMapping
    public Flux<BookDto> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<BookDto> getBook(@PathVariable String id) {
        return bookService.findById(id);
    }

    @PostMapping
    public Mono<ResponseEntity<BookDto>> createBook(@RequestBody @Valid BookCreateDto createDto) {
        return bookService.insert(createDto)
                .map(savedBook -> ResponseEntity.status(HttpStatus.CREATED).body(savedBook));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<BookDto>> updateBook(@PathVariable String id,
                                                    @RequestBody @Valid BookUpdateDto updateDto) {
        if (!id.equals(updateDto.getId())) {
            throw new IllegalArgumentException("Path variable id and BookDto id must match");
        }

        return bookService.update(updateDto)
                .map(updatedBook -> ResponseEntity.ok(updatedBook));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable String id) {
        return bookService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}