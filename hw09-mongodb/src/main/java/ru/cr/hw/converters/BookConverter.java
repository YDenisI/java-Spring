package ru.cr.hw.converters;


import org.springframework.stereotype.Component;
import ru.cr.hw.dto.BookDto;

@Component
public class BookConverter {
    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final CommentConverter commentsConverter;

    public BookConverter(AuthorConverter authorConverter, GenreConverter genreConverter,
                         CommentConverter commentsConverter) {
        this.authorConverter = authorConverter;
        this.genreConverter = genreConverter;
        this.commentsConverter = commentsConverter;
    }

    public String bookToString(BookDto book) {

        String commentsStr = book.getComments() != null && !book.getComments().isEmpty()
                ? book.getComments().stream()
                .map(commentsConverter::commentToString)
                .reduce((a, b) -> a + "\n" + b)
                .orElse("")
                : "Нет комментариев";

        return "\nId: %s, title: %s, author: {%s}, genres: {%s}, comments[%s]".formatted(
                book.getId(),
                book.getTitle(),
                authorConverter.authorToString(book.getAuthor()),
                genreConverter.genreToString(book.getGenre()),
                commentsStr);
    }
}
