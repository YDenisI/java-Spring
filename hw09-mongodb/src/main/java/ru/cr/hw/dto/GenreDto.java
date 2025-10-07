package ru.cr.hw.dto;

import lombok.Data;
import ru.cr.hw.models.Genre;

@Data
public class GenreDto {

    private String id;

    private String name;

    public GenreDto() {

    }

    public GenreDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static GenreDto fromDomain(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new GenreDto(genre.getId(), genre.getName());
    }

    public Genre toDomain() {
        Genre genre = new Genre();
        if (id != null) {
            genre.setId(id);
        }
        genre.setName(this.name);
        return genre;
    }
}
