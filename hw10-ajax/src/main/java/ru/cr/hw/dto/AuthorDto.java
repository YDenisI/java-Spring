package ru.cr.hw.dto;

import ru.cr.hw.domain.Author;

public class AuthorDto {

    private Long id;

    private String name;

    public AuthorDto() {
    }

    public AuthorDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AuthorDto fromDomain(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getFullName()
        );
    }

    public Author toDomain() {
        Author author = new Author();
        if (id != null && id != 0) {
            author.setId(id);
        }
        author.setFullName(this.name);
        return author;
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
