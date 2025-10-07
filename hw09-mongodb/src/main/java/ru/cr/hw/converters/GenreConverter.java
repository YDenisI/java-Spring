package ru.cr.hw.converters;

import org.springframework.stereotype.Component;
import ru.cr.hw.dto.GenreDto;

@Component
public class GenreConverter {
    public String genreToString(GenreDto genre) {
        return "Id: %s, Name: %s".formatted(genre.getId(), genre.getName());
    }
}
