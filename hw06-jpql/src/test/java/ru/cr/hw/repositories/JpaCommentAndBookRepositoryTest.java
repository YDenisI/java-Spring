package ru.cr.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Comment;
import ru.cr.hw.models.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaBookRepository.class, JpaGenreRepository.class, JpaCommentRepository.class})
@DisplayName("Репозиторий на основе JPA для работы с комментариями")
class JpaCommentAndBookRepositoryTest {

    private static final String AUTHOR_NAME_1 = "Author_1";
    private static final String GENRE_NAME_1 = "Genre_1";

    @Autowired
    private JpaCommentRepository commentRepository;

    @Autowired
    private JpaBookRepository bookRepository;

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void findAllByBookIdWithoutTransaction() {
        List<Comment> comments = commentRepository.findByBookId(1L);
        assertThat(comments).isNotEmpty();
        assertThat(comments).extracting(Comment::getComment)
                .containsExactlyInAnyOrder("Comment_1", "Comment_3");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldInsertComment() {
        Comment newComment = new Comment();
        newComment.setComment("Новый комментарий");

        var expectedBook = new Book(1L, "BookTitle_1", new Author(1L, AUTHOR_NAME_1), new Genre(1L, GENRE_NAME_1));
        newComment.setBook(expectedBook);

        Comment savedComment = commentRepository.insert(newComment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isGreaterThan(0);
        assertThat(savedComment)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newComment);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void findAllByIdWithoutTransaction() {
        List<Book> books = bookRepository.findAll();
        assertThat(books)
                .extracting(Book::getTitle)
                .contains("BookTitle_1", "BookTitle_2");
    }
}