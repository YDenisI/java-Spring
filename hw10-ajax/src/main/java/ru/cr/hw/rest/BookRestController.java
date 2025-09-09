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
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookUpdateDto;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping
    public List<BookDto> getAllBooks() {
        List<BookDto> books = bookService.findAll();
        return books;
    }

    @GetMapping("/{id}")
    public BookDto getBook(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(@RequestBody @Valid BookCreateDto createDto) {
        BookDto savedBook = bookService.insert(
                createDto.getTitle(),
                createDto.getAuthorId(),
                createDto.getGenreId());

        if (createDto.getInitialComment() != null && !createDto.getInitialComment().trim().isEmpty()) {
            commentService.insert(createDto.getInitialComment().trim(), savedBook.getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody @Valid BookUpdateDto updateDto) {
        if (!id.equals(updateDto.getId())) {
            throw new IllegalArgumentException("Path variable id and BookDto id must match");
        }

        bookService.update(updateDto.getId(), updateDto.getTitle(), updateDto.getAuthorId(), updateDto.getGenreId());
        BookDto updatedBook = bookService.findById(updateDto.getId());
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}