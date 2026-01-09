package ru.cr.hw.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    private EntityManager entityManager;

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

    Book expectedBook;

    @BeforeEach
    public void setUp() {
        bookService = new BookServiceImpl(authorRepository, genreRepository, bookRepository);
        expectedBook = new Book(BOOK_TITLE_1, new Author(1L, AUTHOR_NAME_1), new Genre(1L, GENRE_NAME_1));
    }
    @Test
    public void testFindById() {

        Optional<Book> optBook = bookRepository.findById(BOOK_ID);

        assertThat(optBook)
                .isPresent()
                .get()
                .satisfies(found -> assertThat(found)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "comments")
                        .isEqualTo(expectedBook));
    }

    @Test
    public void testFindAll() {

        List<Book> books = bookService.findAll();
        assertThat(books).hasSize(3);
       // assertThat(books.get(0).getTitle()).isEqualTo("BookTitle_1");
        assertThat(books.get(0))
                .satisfies(found -> assertThat(found)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "comments")
                        .isEqualTo(expectedBook));
    }

    @Test
    public void testInsert() {
        Book createdBook = bookService.insert("Book Title", AUTHOR_ID_1, GENRE_ID_1);
        assertThat(createdBook.getTitle()).isEqualTo("Book Title");
        Book persisted = entityManager.find(Book.class, createdBook.getId());
        assertThat(persisted).isNotNull();
        assertThat(persisted)
                .satisfies(found -> assertThat(found)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "comments")
                        .isEqualTo(createdBook));
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
        Book updatedBook = bookService.update(BOOK_ID, "Updated Title", AUTHOR_ID_1, GENRE_ID_1);
        Book found = entityManager.find(Book.class, updatedBook.getId());
        assertThat(updatedBook.getTitle()).isEqualTo("Updated Title");
        assertThat(found)
                   .usingRecursiveComparison()
                   .ignoringFields("id", "comments")
                   .isEqualTo(updatedBook);
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
        bookService.deleteById(BOOK_ID);
        Book deleted = entityManager.find(Book.class, BOOK_ID);
        assertThat(deleted).isNull();

        Optional<Book> optDeleted = bookRepository.findById(BOOK_ID);
        assertThat(optDeleted).isNotPresent();
    }
}
