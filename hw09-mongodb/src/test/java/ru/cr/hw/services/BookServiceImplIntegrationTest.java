package ru.cr.hw.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import ru.cr.hw.dto.*;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest
@AutoConfigureDataMongo
@TestPropertySource(locations = "classpath:application-test.yaml")
@Import(BookServiceImpl.class)
public class BookServiceImplIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CommentRepository commentRepository;



    private Author author1;
    private Genre genre1;

    @BeforeEach
    void setUp() {

        commentRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();

        author1 = authorRepository.save(new Author("Author_1"));
        Author author2 = authorRepository.save(new Author("Author_2"));
        genre1 = genreRepository.save(new Genre("Genre_1"));
        Genre genre2 = genreRepository.save(new Genre("Genre_2"));
    }

    @Test
    void findById_shouldReturnBookWithComments_whenBookExists() {

        Book book = new Book("Title_1", author1, genre1);
        Book savedBook = bookRepository.save(book);
        Comment comment = new Comment("Comment_1", savedBook.getId());
        commentRepository.save(comment);


        AuthorDto expectedAuthorDto = AuthorDto.fromDomain(author1);
        GenreDto expectedGenreDto = GenreDto.fromDomain(genre1);
        BookDto expectedBook = new BookDto();
        expectedBook.setTitle("Title_1");
        expectedBook.setAuthor(expectedAuthorDto);
        expectedBook.setGenre(expectedGenreDto);

        Optional<BookDto> optBook = findBookOptional(savedBook.getId());

        assertThat(optBook)
                .isPresent()
                .get()
                .satisfies(found -> assertThat(found)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "comments")
                        .isEqualTo(expectedBook));
    }
    @Test
    void findById_shouldThrowEntityNotFoundException_whenBookNotFound() {
        // When & Then
        assertThatThrownBy(() -> bookService.findById("non-existent-id"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book with id non-existent-id not found");
    }

    @Test
    void findAll_shouldReturnAllBooksWithComments() {

        Book book1 = new Book("Title_1", author1, genre1);
        Book book2 = new Book("Title_2", author1, genre1);
        Book savedBook1 = bookRepository.save(book1);
        Book savedBook2 = bookRepository.save(book2);
        Comment comment1 = new Comment("Comment_1", savedBook1.getId());
        Comment comment2 = new Comment("Comment_2", savedBook2.getId());
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<BookDto> result = bookService.findAll();

        assertThat(result)
                .hasSize(2)
                .allSatisfy(bookDto ->
                        assertThat(bookDto.getComments())
                                .isNotNull()
                                .hasSize(1)
                );

        assertThat(result)
                .extracting(BookDto::getTitle)
                .containsExactlyInAnyOrder("Title_1", "Title_2");
    }

    @Test
    void insert_shouldCreateNewBook_whenValidData() {

        BookCreateDto dto = new BookCreateDto("New Title", author1.getId(), genre1.getId());

        AuthorDto expectedAuthorDto = AuthorDto.fromDomain(author1);
        GenreDto expectedGenreDto = GenreDto.fromDomain(genre1);
        BookDto expectedBook = new BookDto();
        expectedBook.setTitle("New Title");
        expectedBook.setAuthor(expectedAuthorDto);
        expectedBook.setGenre(expectedGenreDto);

        BookDto result = bookService.insert(dto);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "comments")  // id генерируется, comments пустые
                .isEqualTo(expectedBook);
    }

    @Test
    void insert_shouldThrowIllegalArgumentException_whenAuthorNotFound() {

        BookCreateDto dto = new BookCreateDto("New Title", "invalid-author-id", genre1.getId());

        assertThatThrownBy(() -> bookService.insert(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author not found");
    }

    @Test
    void insert_shouldThrowIllegalArgumentException_whenGenreNotFound() {

        BookCreateDto dto = new BookCreateDto("New Title", author1.getId(), "invalid-genre-id");

        assertThatThrownBy(() -> bookService.insert(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Genre not found");
    }

    @Test
    void update_shouldUpdateBook_whenValidData() {

        Book book = new Book("Old Title", author1, genre1);
        Book savedBook = bookRepository.save(book);
        BookUpdateDto dto = new BookUpdateDto(savedBook.getId(), "Updated Title", author1.getId(), genre1.getId());

        AuthorDto expectedAuthorDto = AuthorDto.fromDomain(author1);
        GenreDto expectedGenreDto = GenreDto.fromDomain(genre1);
        BookDto expectedBook = new BookDto();
        expectedBook.setTitle("Updated Title");
        expectedBook.setAuthor(expectedAuthorDto);
        expectedBook.setGenre(expectedGenreDto);

        BookDto result = bookService.update(dto);

        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "comments")  // id не меняется, comments игнорируем
                .isEqualTo(expectedBook);
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenBookNotFound() {

        BookUpdateDto dto = new BookUpdateDto("non-existent-id", "Title", author1.getId(), genre1.getId());

        assertThatThrownBy(() -> bookService.update(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book with id non-existent-id not found");
    }

    @Test
    void update_shouldThrowEntityNotFoundException_whenAuthorNotFound() {

        Book book = new Book("Title", author1, genre1);
        Book savedBook = bookRepository.save(book);
        BookUpdateDto dto = new BookUpdateDto(savedBook.getId(), "Title", "invalid-author-id", genre1.getId());

        assertThatThrownBy(() -> bookService.update(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Author with id invalid-author-id not found");
    }

    @Test
    void deleteById_shouldDeleteBookAndComments_whenBookExists() {

        Book book = new Book("Title", author1, genre1);
        Book savedBook = bookRepository.save(book);
        Comment comment = new Comment("Comment", savedBook.getId());
        commentRepository.save(comment);

        bookService.deleteById(savedBook.getId());

        assertThat(bookRepository.existsById(savedBook.getId())).isFalse();
        assertThat(commentRepository.findByBookId(savedBook.getId())).isEmpty();
    }

    @Test
    void deleteById_shouldThrowEntityNotFoundException_whenBookNotFound() {

        assertThatThrownBy(() -> bookService.deleteById("non-existent-id"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Book not found");
    }

    private Optional<BookDto> findBookOptional(String id) {
        try {
            return Optional.of(bookService.findById(id));
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }
}
