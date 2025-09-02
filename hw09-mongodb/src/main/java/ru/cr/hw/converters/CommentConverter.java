package ru.cr.hw.converters;

import org.springframework.stereotype.Component;
import ru.cr.hw.models.Comment;

@Component
public class CommentConverter {
    public CommentConverter() {
    }

    public String commentToString(Comment comment) {
        return "Id: %d, Comment: %s".formatted(
                comment.getId(),
                comment.getComment());
    }
}
