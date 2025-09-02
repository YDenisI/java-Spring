package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import ru.cr.hw.domain.Author;
import ru.cr.hw.domain.Book;
import ru.cr.hw.domain.Genre;

public class BookDto {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    private Long authorId;

    private Long genreId;

    private String initialComment;

    public BookDto() {
    }

    public BookDto(Long id, String title, Long authorId, Long genreId, String initialComment) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
        this.initialComment = initialComment;
    }

    public static BookDto fromDomain(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getId(),
                book.getGenre().getId(),
                null
        );
    }

    public Book toDomain(Author author, Genre genre) {
        Book book = new Book();
        if (this.id != null && this.id != 0) {
            book.setId(this.id);
        }
        book.setTitle(this.title);
        book.setAuthor(author);
        book.setGenre(genre);
        return book;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getInitialComment() {
        return initialComment;
    }

    public void setInitialComment(String initialComment) {
        this.initialComment = initialComment;
    }
}