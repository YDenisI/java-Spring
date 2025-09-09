package ru.cr.hw.dto;

import ru.cr.hw.domain.Genre;

public class GenreDto {
    private Long id;

    private String name;

    public GenreDto() {

    }

    public GenreDto(Long id, String name) {
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
        if (id != null && id != 0) {
            genre.setId(id);
        }
        genre.setName(this.name);
        return genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
