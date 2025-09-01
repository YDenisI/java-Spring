package ru.cr.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Comment;
import ru.cr.hw.models.Genre;
import ru.cr.hw.repositories.BookRepository;
import ru.cr.hw.repositories.CommentRepository;
import ru.cr.hw.services.CommentServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class CommentServiceImplTest {
    private static final long COMMENT_ID = 1L;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BookRepository bookRepository;

    private CommentServiceImpl commentService;

    private Book book;
    private Comment newComment;


    @BeforeEach
    public void setUp() {
        commentService = new CommentServiceImpl(commentRepository, bookRepository);

        newComment = commentService.insert("Old comment", 1L);
    }

    @Test
    void findAllByBookId() {
        List<Comment> comments = commentService.findByBookId(1L);
        assertThat(comments).isNotEmpty();
        assertThat(comments).extracting(Comment::getComment)
                .containsExactlyInAnyOrder("Comment_1", "Comment_3","Old comment");
    }

    @Test
    public void testInsertComment_Success() {

        String commentText = "This is a test comment";

        Comment createdComment = commentService.insert(commentText, COMMENT_ID);
        assertNotNull(createdComment);
        assertEquals(commentText, createdComment.getComment());
        assertEquals(COMMENT_ID, createdComment.getBook().getId());

        assertThat(createdComment.getComment()).isEqualTo(commentText);
    }

    @Test
    public void testInsertComment_BookNotFound() {

        String commentText = "This is a test comment";
        long nonExistentBookId = 99L;

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            commentService.insert(commentText, nonExistentBookId);
        });
        assertEquals("Book with id " + nonExistentBookId + " not found", exception.getMessage());
    }


    @Test
    @Transactional
    public void testUpdateComment_Success() {

        String updatedCommentText = "Updated comment";

        Comment updatedComment = commentService.update(newComment.getId(), updatedCommentText);

        assertNotNull(updatedComment);
        assertEquals(updatedCommentText, updatedComment.getComment());
    }

    @Test
    public void testUpdateComment_NotFound() {

        long nonExistentCommentId = 99L;
        String updatedCommentText = "Updated comment";

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            commentService.update(nonExistentCommentId, updatedCommentText);
        });
        assertEquals("Comment with id " + nonExistentCommentId + " not found", exception.getMessage());
    }

    @Test
    public void testDeleteComment_Success() {

        var existingComment = commentService.insert("Test comment", COMMENT_ID);

        commentService.deleteById(existingComment.getId());

        assertFalse(commentService.findById(existingComment.getId()).isPresent());
    }

    @Test
    public void testFindById_Success() {

        var existingComment = commentService.insert("Test comment", COMMENT_ID);

        Optional<Comment> foundComment = commentService.findById(existingComment.getId());

        assertTrue(foundComment.isPresent());
        assertEquals(existingComment.getComment(), foundComment.get().getComment());
    }

    @Test
    public void testFindById_NotFound() {

        long nonExistentCommentId = 999L;

        Optional<Comment> foundComment = commentService.findById(nonExistentCommentId);

        assertFalse(foundComment.isPresent());
    }
}
