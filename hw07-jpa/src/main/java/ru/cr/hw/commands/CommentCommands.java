package ru.cr.hw.commands;

import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.cr.hw.converters.CommentConverter;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Comment;
import ru.cr.hw.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;


    @ShellMethod(value = "Find comment", key = "fc")
    public String findById(long id) {
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find all comment for book", key = "fcb")
    public String findByBookId(long id) {
        try {
            List<Comment> comments = commentService.findByBookId(id);
            if (!comments.isEmpty()) {
                return comments.stream()
                        .map(commentConverter::commentToString)
                        .collect(Collectors.joining("," + System.lineSeparator()));
            } else {
                return "Comments for Book with id %d not found".formatted(id);
            }
        } catch (EntityNotFoundException | NoResultException e) {
            return "Comments for Book with id %d not found".formatted(id);
        }
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String comment, long bookId) {
        var savedComment = commentService.insert(comment, bookId);
        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Update commet", key = "cupd")
    public String updateComment(long id, String comment) {
        var savedComment = commentService.update(id, comment);
        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(long id) {
        commentService.deleteById(id);
    }
}
