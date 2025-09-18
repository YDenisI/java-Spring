package ru.cr.hw.rest;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.cr.hw.dto.CommentCreateDto;
import ru.cr.hw.dto.CommentUpdateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;

import java.net.URI;
import java.util.List;

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
    public List<CommentDto> getComments(@PathVariable Long bookId) {
     //   BookDto book = bookService.findById(bookId);

        List<CommentDto> comments = commentService.findByBookId(bookId);

        return commentService.findByBookId(bookId);
    }

    @PostMapping("/api/books/{bookId}/comments")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long bookId,
                                                 @Valid @RequestBody CommentCreateDto newCommentDto) {
        if (!bookId.equals(newCommentDto.getBookId())) {
            throw new IllegalArgumentException("Path variable bookId and newCommentDto id must match");
        }
        CommentDto created = commentService.insert(newCommentDto);
        return ResponseEntity.created(URI.create("/api/comments/" + created.getId()))
                .body(created);
    }

    @PutMapping("/api/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId,
                                                    @RequestBody CommentUpdateDto updatedCommentDto) {

        if (!commentId.equals(updatedCommentDto.getId())) {
            throw new IllegalArgumentException("Path variable commentId and updatedCommentDto id must match");
        }
        commentService.update(updatedCommentDto);
        CommentDto updated = commentService.findById(commentId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        CommentDto comment = commentService.findById(commentId);
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }
}