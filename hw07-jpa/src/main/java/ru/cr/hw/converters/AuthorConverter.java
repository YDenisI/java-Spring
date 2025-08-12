package ru.cr.hw.converters;

import org.springframework.stereotype.Component;
import ru.cr.hw.models.Author;

@Component
public class AuthorConverter {
    public String authorToString(Author author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}
