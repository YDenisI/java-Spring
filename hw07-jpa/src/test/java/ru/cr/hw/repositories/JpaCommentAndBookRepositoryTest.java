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
@DisplayName("Репозиторий на основе JPA для работы с комментариями")
class JpaCommentAndBookRepositoryTest {

    private static final String AUTHOR_NAME_1 = "Author_1";
    private static final String GENRE_NAME_1 = "Genre_1";

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void findAllByBookId() {
        List<Comment> comments = commentRepository.findByBookId(1L);
        assertThat(comments).isNotEmpty();
        assertThat(comments).extracting(Comment::getComment)
                .containsExactlyInAnyOrder("Comment_1", "Comment_3");
    }

    @Test
    void shouldInsertComment() {
        Comment newComment = new Comment();
        newComment.setComment("Новый комментарий");

        var expectedBook = new Book("BookTitle_1", new Author(1L, AUTHOR_NAME_1), new Genre(1L, GENRE_NAME_1));
        newComment.setBook(expectedBook);

        Comment savedComment = commentRepository.save(newComment);

        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isGreaterThan(0);
        assertThat(savedComment)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(newComment);
    }

}