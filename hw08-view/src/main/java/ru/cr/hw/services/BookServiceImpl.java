package ru.cr.hw.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.controller.NotFoundException;
import ru.cr.hw.domain.Book;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.repostory.AuthorRepository;
import ru.cr.hw.repostory.BookRepository;
import ru.cr.hw.repostory.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @Override
    public BookDto findById(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        return BookDto.fromDomain(book);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(BookDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto insert(String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        Book book = new Book(title, author, genre);
        Book saved = bookRepository.save(book);
        return BookDto.fromDomain(saved);
    }

    @Override
    @Transactional
    public BookDto update(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        Book book;
        book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(id)));
        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        Book updated = bookRepository.save(book);
        return BookDto.fromDomain(updated);
    }

    @Override
    public void deleteById(long id) {
        boolean exists = bookRepository.existsById(id);
        if (!exists) {
            throw new NotFoundException();
        }
        bookRepository.deleteById(id);
    }
}
