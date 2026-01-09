package ru.cr.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.cr.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    public String commentToString(Comment comment) {
        return "Id: %d, Comment: %s".formatted(
                comment.getId(),
                comment.getComment());
    }
}
