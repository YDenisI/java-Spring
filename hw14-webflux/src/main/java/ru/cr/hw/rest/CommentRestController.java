package ru.cr.hw.rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.cr.hw.dto.CommentCreateDto;
import ru.cr.hw.dto.CommentUpdateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;


@Slf4j
@RestController
public class CommentRestController {

    private final CommentService commentService;

    private final BookService bookService;

    public CommentRestController(CommentService commentService, BookService bookService) {
        this.commentService = commentService;
        this.bookService = bookService;
    }

    @GetMapping("/api/books/{bookId}/comments")
    public Flux<CommentDto> getComments(@PathVariable String bookId) {
        return commentService.findByBookId(bookId);//.map(updated -> ResponseEntity.ok(updated));
    }

    @PostMapping("/api/books/{bookId}/comments")
    public Mono<ResponseEntity<CommentDto>> addComment(@PathVariable String bookId,
                                                       @Valid @RequestBody CommentCreateDto newCommentDto) {
        if (!bookId.equals(newCommentDto.getBookId())) {
            throw new IllegalArgumentException("Path variable bookId and newCommentDto id must match");
        }
        return commentService.create(newCommentDto)
                .map(savedComment -> ResponseEntity.status(HttpStatus.CREATED).body(savedComment));
    }

    @PutMapping("/api/comments/{commentId}")
    public Mono<ResponseEntity<CommentDto>> updateComment(@PathVariable String commentId,
                                                    @RequestBody CommentUpdateDto updatedCommentDto) {

        if (!commentId.equals(updatedCommentDto.getId())) {
            throw new IllegalArgumentException("Path variable commentId and updatedCommentDto id must match");
        }
        return commentService.update(updatedCommentDto)
            .map(updated -> ResponseEntity.ok(updated));
    }

    @DeleteMapping("/api/comments/{commentId}")
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable String commentId) {
        return commentService.deleteById(commentId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}