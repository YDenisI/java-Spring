package ru.cr.hw.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.cr.hw.converters.BookConverter;
import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookUpdateDto;
import ru.cr.hw.services.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    public BookCommands(BookService bookService, BookConverter bookConverter) {
        this.bookService = bookService;
        this.bookConverter = bookConverter;
    }

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll().stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }

    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookConverter.bookToString(bookService.findById(id));

    }

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, String genreId) {
        var bookCreateDto = new BookCreateDto(title, authorId,genreId);
        var savedBook = bookService.insert(bookCreateDto);
        return bookConverter.bookToString(savedBook);
    }

    // bupd 4 editedBook 3 2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, String genreId) {
        var bookUpdateDto = new BookUpdateDto(id, title, authorId,genreId);
        var savedBook = bookService.update(bookUpdateDto);
        return bookConverter.bookToString(savedBook);
    }

    // bdel 4
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }

    // bdel 4
 /*   @ShellMethod(value = "Find book by author", key = "fba")
    public String findBookbyAuthor(String id) {
        return bookService.findByAuthor(id).stream()
                .map(bookConverter::bookToString)
                .collect(Collectors.joining("," + System.lineSeparator()));

    }*/

}
