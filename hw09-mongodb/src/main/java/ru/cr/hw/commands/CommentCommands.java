package ru.cr.hw.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.cr.hw.converters.CommentConverter;
import ru.cr.hw.dto.CommentCreateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.dto.CommentUpdateDto;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;


@ShellComponent
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    public CommentCommands(CommentService commentService, CommentConverter commentConverter) {
        this.commentService = commentService;
        this.commentConverter = commentConverter;
    }

    @ShellMethod(value = "Find comment", key = "fc")
    public String findById(String id) {
        return  commentConverter.commentToString(commentService.findById(id));
    }

    @ShellMethod(value = "Find all comment for book", key = "fcb")
    public String findByBookId(String id) {
        try {
            List<CommentDto> comments = commentService.findByBookId(id);
            if (!comments.isEmpty()) {
                return comments.stream()
                        .map(commentConverter::commentToString)
                        .collect(Collectors.joining("," + System.lineSeparator()));
            } else {
                return "Comments for Book with id %s not found".formatted(id);
            }
        } catch (EntityNotFoundException e) {
            return "Comments for Book with id %s not found".formatted(id);
        }
    }

    @ShellMethod(value = "Insert comment", key = "cins")
    public String insertComment(String comment, String bookId) {
        var commentCreateDto = new CommentCreateDto(comment,bookId);
        var savedComment = commentService.insert(commentCreateDto);
        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Update commet", key = "cupd")
    public String updateComment(String id, String comment) {
        var commentUpdateDto = new CommentUpdateDto(id, comment);
        var savedComment = commentService.update(commentUpdateDto);
        return commentConverter.commentToString(savedComment);
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteComment(String id) {
        commentService.deleteById(id);
    }
}
