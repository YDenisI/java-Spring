package ru.cr.hw.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.controller.NotFoundException;
import ru.cr.hw.domain.Book;
import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.BookUpdateDto;
import ru.cr.hw.dto.CommentCreateDto;
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

    private final CommentService commentService;

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
    public BookDto insert(BookCreateDto createDto) {
        String title = createDto.getTitle();
        Long authorId = createDto.getAuthorId();
        Long genreId = createDto.getGenreId();
        var author = authorRepository.findById(createDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(createDto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        Book book;
        book = new Book(title, author, genre);
        Book savedBook = bookRepository.save(book);

        CommentCreateDto commentDto = createDto.getInitialComment();
        if (commentDto != null) {
            commentDto.setBookId(savedBook.getId());
            commentService.insert(commentDto);
        }

        return BookDto.fromDomain(savedBook);
    }

    @Override
    @Transactional
    public BookDto update(BookUpdateDto updateDto) {
        Long id = updateDto.getId();
        String title = updateDto.getTitle();
        Long authorId = updateDto.getAuthorId();
        Long genreId = updateDto.getGenreId();
        var author = authorRepository.findById(updateDto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(updateDto.getGenreId())
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        Book book;
        book = bookRepository.findById(updateDto.getId())
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
            throw new NotFoundException("Book not Found");
        }
        bookRepository.deleteById(id);
    }
}
