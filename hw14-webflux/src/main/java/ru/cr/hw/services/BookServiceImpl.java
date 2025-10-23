package ru.cr.hw.services;


import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import ru.cr.hw.domain.Author;
import ru.cr.hw.domain.Book;
import ru.cr.hw.domain.Comment;

import ru.cr.hw.domain.Genre;
import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.BookUpdateDto;
import ru.cr.hw.repostory.AuthorRepository;
import ru.cr.hw.repostory.BookRepository;
import ru.cr.hw.repostory.CommentRepository;
import ru.cr.hw.repostory.GenreRepository;
import ru.cr.hw.rest.exceptions.NotFoundException;



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
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .map(BookDto::fromDomain);
    }

    @Override
    public Flux<BookDto> findAll() {
         return bookRepository.findAll()
                .map(BookDto::fromDomain);

    }

    @Override
    public Mono<BookDto> create(BookCreateDto bookCreateDto) {
        Mono<Author> authorMono = authorRepository.findById(bookCreateDto.getAuthorId())
                .switchIfEmpty(Mono.error(new NotFoundException("Author with id " + bookCreateDto.getAuthorId() + " not found")));

        Mono<Genre> genreMono = genreRepository.findById(bookCreateDto.getGenreId())
                .switchIfEmpty(Mono.error(new NotFoundException("Genre with id " + bookCreateDto.getGenreId() + " not found")));

        return Mono.zip(authorMono, genreMono)
                .flatMap(tuple -> {
                    Author author = tuple.getT1();
                    Genre genre = tuple.getT2();
                    Book book = new Book(bookCreateDto.getTitle(), author, genre);
                    return bookRepository.save(book)
                            .flatMap(savedBook -> {
                                Comment comment = new Comment(bookCreateDto.getInitialComment(), savedBook);
                                return commentRepository.save(comment).thenReturn(savedBook);
                            });
                })
                .map(BookDto::fromDomain);
    }

    @Override
    public Mono<BookDto> update(BookUpdateDto bookUpdateDto) {
        Mono<Author> authorMono = authorRepository.findById(bookUpdateDto.getAuthorId())
                .switchIfEmpty(Mono.error(new NotFoundException("Author with id " + bookUpdateDto.getAuthorId() + " not found")));

        Mono<Genre> genreMono = genreRepository.findById(bookUpdateDto.getGenreId())
                .switchIfEmpty(Mono.error(new NotFoundException("Genre with id " + bookUpdateDto.getGenreId() + " not found")));

        Mono<Book> bookMono = bookRepository.findById(bookUpdateDto.getId())
                .switchIfEmpty(Mono.error(new NotFoundException("Book with id " + bookUpdateDto.getId() + " not found")));

        return Mono.zip(authorMono, genreMono, bookMono)
                .flatMap(tuple -> {
                    Author author = tuple.getT1();
                    Genre genre = tuple.getT2();
                    Book book = tuple.getT3();
                    book.setTitle(bookUpdateDto.getTitle());
                    book.setAuthor(author);
                    book.setGenre(genre);
                    return bookRepository.save(book);
                })
                .map(BookDto::fromDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return bookRepository.existsById(id)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NotFoundException("Book with id " + id + " not found"));
                    }

                    return commentRepository.findByBookId(id)
                            .flatMap(comment -> commentRepository.delete(comment))
                            .then()
                            .then(bookRepository.deleteById(id));
                });
    }
}
