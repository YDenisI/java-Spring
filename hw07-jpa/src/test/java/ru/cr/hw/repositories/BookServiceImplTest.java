package ru.cr.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Author;
import ru.cr.hw.models.Genre;
import ru.cr.hw.repositories.AuthorRepository;
import ru.cr.hw.repositories.BookRepository;
import ru.cr.hw.repositories.GenreRepository;
import ru.cr.hw.services.BookServiceImpl;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class BookServiceImplTest {
    private static final long BOOK_ID = 1L;
    private static final String BOOK_TITLE_1 = "BookTitle_1";
    private static final String AUTHOR_NAME_1 = "Author_1";
    private static final String GENRE_NAME_1 = "Genre_1";
    private static final long AUTHOR_ID_1 = 1L;
    private static final long GENRE_ID_1 = 1L;
    private static final String BOOK_TITLE_2 = "BookTitle_2";

    private static final String BOOK_TITLE_3 = "BookTitle_3";

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookRepository bookRepository;

    private BookServiceImpl bookService;

    private Book book;
    private Author author;
    private Genre genre;

    @BeforeEach
    public void setUp() {
        bookService = new BookServiceImpl(authorRepository, genreRepository, bookRepository);
        book = bookService.insert(BOOK_TITLE_1, AUTHOR_ID_1, GENRE_ID_1);
    }
    @Test
    public void testFindById() {

        Optional<Book> foundBook = bookService.findById(book.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("BookTitle_1");
    }

    @Test
    public void testFindAll() {

        List<Book> books = bookService.findAll();
        assertThat(books).hasSize(4);
        assertThat(books.get(0).getTitle()).isEqualTo("BookTitle_1");
    }

    @Test
    public void testInsert() {
        Book createdBook = bookService.insert("Book Title", AUTHOR_ID_1, GENRE_ID_1);
        assertThat(createdBook.getTitle()).isEqualTo("Book Title");
    }

    @Test
    public void testInsertAuthorNotFound() {
        long nonExistentAuthorId = 99L;

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.insert("Book Title", nonExistentAuthorId, GENRE_ID_1);
        });

        assertThat(exception.getMessage()).isEqualTo("Author with id 99 not found");
    }

    @Test
    public void testUpdate() {
        Book updatedBook = bookService.update(book.getId(), "Updated Title", AUTHOR_ID_1, GENRE_ID_1);
        assertThat(updatedBook.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    public void testUpdateBookNotFound() {
        long nonExistentBookId = 99L;

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            bookService.update(nonExistentBookId, "Updated Title", AUTHOR_ID_1, GENRE_ID_1);
        });

        assertThat(exception.getMessage()).isEqualTo("Book with id 99 not found");
    }

    @Test
    public void testDeleteById() {
        bookService.deleteById(book.getId());
        Optional<Book> deletedBook = bookRepository.findById(book.getId());
        assertThat(deletedBook).isNotPresent();
    }
}
