package ru.cr.hw.converters;

import org.springframework.stereotype.Component;
import ru.cr.hw.dto.CommentDto;

@Component
public class CommentConverter {
    public CommentConverter() {
    }

    public String commentToString(CommentDto comment) {
        return "{Id: %s, Comment: %s, bookId: %s}".formatted(
                comment.getId(),
                comment.getComment(),
                comment.getBookId());
    }
}
