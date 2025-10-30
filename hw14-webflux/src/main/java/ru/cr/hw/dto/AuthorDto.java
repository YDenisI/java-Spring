package ru.cr.hw.dto;

import lombok.Data;
import ru.cr.hw.domain.Author;

@Data
public class AuthorDto {

    private String id;

    private String name;

    public AuthorDto() {
    }

    public AuthorDto(String id, String name) {
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
        if (id != null) {
            author.setId(id);
        }
        author.setFullName(this.name);
        return author;
    }
}
