package ru.cr.hw.dto;

import lombok.Data;
import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;

import ru.cr.hw.models.Genre;

@Data
public class BookDto {

    private String id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    public BookDto() {

    }

    public BookDto(String id, String title, AuthorDto author, GenreDto genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public static BookDto fromDomain(Book book) {
        if (book == null) {
            return null;
        }

        AuthorDto authorDTO = AuthorDto.fromDomain(book.getAuthor());
        GenreDto genreDTO = GenreDto.fromDomain(book.getGenre());

        return new BookDto(book.getId(), book.getTitle(), authorDTO, genreDTO);
    }

    public static Book toDomain(BookDto dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());

        if (dto.getAuthor() != null) {
            Author author = new Author();
            author.setId(dto.getAuthor().getId());
            author.setFullName(dto.getAuthor().getName());
            book.setAuthor(author);
        } else {
            book.setAuthor(null);
        }

        if (dto.getGenre() != null) {
            Genre genre = new Genre();
            genre.setId(dto.getAuthor().getId());
            genre.setName(dto.getGenre().getName());
            book.setGenre(genre);
        } else {
            book.setGenre(null);
        }
        return book;

    }
}