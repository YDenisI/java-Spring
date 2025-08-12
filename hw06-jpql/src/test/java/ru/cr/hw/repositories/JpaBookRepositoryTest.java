package ru.cr.hw.repositories;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Comment;
import ru.cr.hw.models.Genre;
import ru.cr.hw.repositories.JpaBookRepository;
import ru.cr.hw.repositories.JpaGenreRepository;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.BookServiceImpl;
import ru.cr.hw.services.CommentService;
import ru.cr.hw.services.CommentServiceImpl;

import java.util.List;
import java.util.Optional;


import static java.util.Objects.isNull;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class, JpaCommentRepository.class})
class JpaBookRepositoryTest {
    private static final long BOOK_ID = 1L;
    private static final String BOOK_TITLE_1 = "BookTitle_1";
    private static final String AUTHOR_NAME_1 = "Author_1";
    private static final String GENRE_NAME_1 = "Genre_1";

    private static final String BOOK_TITLE_2 = "BookTitle_2";

    private static final String BOOK_TITLE_3 = "BookTitle_3";

    @Autowired
    private TestEntityManager em;

    private JpaBookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = new JpaBookRepository(em.getEntityManager());
    }

    @Test
    void findByIdReturnsBookWithAuthorAndGenre() {

        var expectedBook = new Book(1L, "BookTitle_1", new Author(1L, AUTHOR_NAME_1), new Genre(1L, GENRE_NAME_1));

        Optional<Book> optBook = bookRepository.findById(BOOK_ID);

        assertThat(optBook)
                .isPresent()
                .get()
                .satisfies(found -> assertThat(found)
                        .usingRecursiveComparison()
                        .ignoringFields("comments", "author.books", "genre.books")
                        .isEqualTo(expectedBook));
    }

    @Test
    void findAllReturnsAllBooks() {

        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(3);

        assertThat(books)
                .extracting(Book::getTitle)
                .contains(BOOK_TITLE_1, BOOK_TITLE_2, BOOK_TITLE_3);
    }

    @Test
    void savePersistsNewBook() {

        Author author = em.find(Author.class, 1L);
        Genre genre = em.find(Genre.class, 1L);

        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor(author);
        book.setGenre(genre);

        Book saved = bookRepository.save(book);
        Book found = em.find(Book.class, saved.getId());

        assertThat(found).isNotNull();

        assertThat(found)
                .usingRecursiveComparison()
                .ignoringFields("comments")
                .isEqualTo(saved);
    }

    @Test
    void deleteByIdRemovesBook() {

        bookRepository.deleteById(BOOK_ID);

        Book deleted = em.find(Book.class, BOOK_ID);
        assertThat(deleted).isNull();
    }
}
