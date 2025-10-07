package ru.cr.hw.services;

import org.springframework.stereotype.Service;
import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.BookUpdateDto;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Comment;
import ru.cr.hw.models.Genre;
import ru.cr.hw.repositories.AuthorRepository;
import ru.cr.hw.repositories.BookRepository;
import ru.cr.hw.repositories.CommentRepository;
import ru.cr.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    public BookServiceImpl(AuthorRepository authorRepository, GenreRepository genreRepository,
                           BookRepository bookRepository, CommentRepository commentRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public BookDto findById(String id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));

        return BookDto.fromDomain(book);
    }

    @Override
    public List<BookDto> findAll() {
        List<BookDto> books = bookRepository.findAll().stream()
                .map(BookDto::fromDomain)
                .collect(Collectors.toList());
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Books not found");
        }
        return books;
    }

    @Override
    public BookDto insert(BookCreateDto bookCreateDto) {
        Author author = authorRepository.findById(bookCreateDto.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author not found"));
        Genre genre = genreRepository.findById(bookCreateDto.getGenreId())
                .orElseThrow(() -> new IllegalArgumentException("Genre not found"));
        Book book = new Book(bookCreateDto.getTitle(), author, genre);
        Book savedBook = bookRepository.save(book);
        return BookDto.fromDomain(savedBook);
    }

    @Override
    public BookDto update(BookUpdateDto bookUpdateDto) {
        String id = bookUpdateDto.getId();
        String title = bookUpdateDto.getTitle();
        String authorId = bookUpdateDto.getAuthorId();
        String genreId = bookUpdateDto.getGenreId();
        var author = authorRepository.findById(bookUpdateDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(bookUpdateDto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        Book book;
        book = bookRepository.findById(bookUpdateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(id)));
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        Book updated = bookRepository.save(book);

        return BookDto.fromDomain(updated);
    }

    @Override
    public void deleteById(String id) {
        boolean exists = bookRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("Book not found");
        }
        List<Comment> comments = commentRepository.findByBookId(id);
        commentRepository.deleteAll(comments);

        bookRepository.deleteById(id);
    }
}
