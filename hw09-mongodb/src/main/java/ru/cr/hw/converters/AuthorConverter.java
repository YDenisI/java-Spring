package ru.cr.hw.converters;

import org.springframework.stereotype.Component;
import ru.cr.hw.dto.AuthorDto;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDto author) {
        return "Id: %s, FullName: %s".formatted(author.getId(), author.getName());
    }
}
