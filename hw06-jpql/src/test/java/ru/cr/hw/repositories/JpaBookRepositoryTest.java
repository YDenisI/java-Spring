package ru.cr.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Genre;
import ru.cr.hw.repositories.JpaBookRepository;
import ru.cr.hw.repositories.JpaGenreRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class})
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

        em.clear();

        Optional<Book> optBook = bookRepository.findById(BOOK_ID);

        assertThat(optBook).isPresent();
        Book found = optBook.get();
        assertThat(found.getTitle()).isEqualTo(BOOK_TITLE_1);
        assertThat(found.getAuthor().getFullName()).isEqualTo(AUTHOR_NAME_1);
        assertThat(found.getGenre().getName()).isEqualTo(GENRE_NAME_1);
    }

    @Test
    void findAllReturnsAllBooks() {
        em.clear();

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

        em.flush();
        em.clear();

        Book found = em.find(Book.class, saved.getId());
        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("New Book");
        assertThat(found.getAuthor().getId()).isEqualTo(author.getId());
        assertThat(found.getGenre().getId()).isEqualTo(genre.getId());
    }

    @Test
    void deleteByIdRemovesBook() {

        bookRepository.deleteById(BOOK_ID);
        em.flush();
        em.clear();

        Book deleted = em.find(Book.class, BOOK_ID);
        assertThat(deleted).isNull();
    }
}
