package ru.cr.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Comment;
import ru.cr.hw.models.Genre;

import java.util.List;
import java.util.Optional;


@Repository
public class JpaCommentRepository implements CommentRepository {

    private static final String SQL_QUERY_COMMENT_BOOK = "select c from Comment c " +
            "join fetch c.book b " +
            "join fetch b.author " +
            "join fetch b.genre " +
            "where b.id = :id";

    private static final String SQL_QUERY_COMMENT_FIND_BY_ID = "select c from Comment c " +
            "join fetch c.book b " +
            "join fetch b.author " +
            "join fetch b.genre " +
            "where c.id = :id";

    @PersistenceContext
    private final EntityManager em;

    public JpaCommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Comment>  findByBookId(long bookId) {
        return em.createQuery(
                        SQL_QUERY_COMMENT_BOOK, Comment.class)
                .setParameter("id", bookId)
                .getResultList();
    }

    @Override
    @Transactional
    public Optional<Comment> findById(long id) {
        TypedQuery<Comment> query = em.createQuery(
                SQL_QUERY_COMMENT_FIND_BY_ID
                , Comment.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Comment insert(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Transactional
    public Comment update(Comment comment) {
        Comment managerComment = em.find(Comment.class, comment.getId());
        if (managerComment == null) {
            throw new EntityNotFoundException("Comment with id " + comment.getId() + " not found");
        }
        managerComment.setComment(comment.getComment());

        Book managedBook = em.find(Book.class, comment.getBook().getId());
        if (managedBook == null) {
            throw new EntityNotFoundException("Book with id " + comment.getBook().getId() + " not found");
        }

        Author managedAuthor = em.find(Author.class, managedBook.getAuthor().getId());
        if (managedAuthor == null) {
            throw new EntityNotFoundException("Author with id " + managedBook.getAuthor().getId() + " not found");
        }
        managedBook.setAuthor(managedAuthor);
        Genre managedGenre = em.find(Genre.class, managedBook.getGenre().getId());
        if (managedGenre == null) {
            throw new EntityNotFoundException("Genre with id " + managedBook.getGenre().getId() + " not found");
        }
        managedBook.setGenre(managedGenre);
        managerComment.setBook(managedBook);
        return managerComment;
    }

    @Override
    public void deleteById(long id) {
        Comment comment = em.find(Comment.class, id);
        if (comment != null) {
            em.remove(comment);
        }
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            return insert(comment);
        }
        return update(comment);
    }
}
