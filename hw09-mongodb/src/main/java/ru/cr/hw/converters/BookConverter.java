package ru.cr.hw.converters;


import org.springframework.stereotype.Component;
import ru.cr.hw.dto.BookDto;

@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    public BookConverter(AuthorConverter authorConverter, GenreConverter genreConverter,
                         CommentConverter commentsConverter) {
        this.authorConverter = authorConverter;
        this.genreConverter = genreConverter;
    }

    public String bookToString(BookDto book) {

        return "\nId: %s, title: %s, author: {%s}, genres: {%s}".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genreConverter.genreToString(book.getGenre()));
    }
}
