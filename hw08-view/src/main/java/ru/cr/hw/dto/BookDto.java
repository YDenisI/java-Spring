package ru.cr.hw.dto;

import ru.cr.hw.domain.Author;
import ru.cr.hw.domain.Book;
import ru.cr.hw.domain.Genre;

public class BookDto {

    private Long id;

    private String title;

    private AuthorDto author;

    private GenreDto genre;

    private String initialComment;

    public BookDto() {

    }

    public BookDto(Long id, String title, AuthorDto author, GenreDto genre, String initialComment) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.initialComment = initialComment;
    }


    public static BookDto fromDomain(Book book) {
        if (book == null) {
            return null;
        }

        AuthorDto authorDTO = new AuthorDto(book.getAuthor().getId(), book.getAuthor().getFullName());
        GenreDto genreDTO = new GenreDto(book.getGenre().getId(), book.getGenre().getName());
        return new BookDto(book.getId(), book.getTitle(), authorDTO, genreDTO, null);
    }

    public Book toDomain(BookDto dto) {
        Book book = new Book();
        book.setId(this.id);
        book.setTitle(this.title);

        if (this.author != null) {
            Author author = new Author();
            author.setId(this.author.getId());
            author.setFullName(this.author.getName());
            book.setAuthor(author);
        } else {
            book.setAuthor(null);
        }

        if (this.genre != null) {
            Genre genre = new Genre();
            genre.setId(this.genre.getId());
            genre.setName(this.genre.getName());
            book.setGenre(genre);
        } else {
            book.setGenre(null);
        }
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

    public AuthorDto getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDto author) {
        this.author = author;
    }

    public GenreDto getGenre() {
        return genre;
    }

    public void setGenre(GenreDto genre) {
        this.genre = genre;
    }

    public String getInitialComment() {
        return initialComment;
    }

    public void setInitialComment(String initialComment) {
        this.initialComment = initialComment;
    }
}