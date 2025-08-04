package ru.cr.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.cr.hw.models.Comment;

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
}
