package ru.cr.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.cr.hw.models.Comment;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(Comment comment) {
        return "Book: {%s}, Id: %d, Comment: %s".formatted(bookConverter.bookToString(comment.getBook()),
                comment.getId(),
                comment.getComment());
    }
}
