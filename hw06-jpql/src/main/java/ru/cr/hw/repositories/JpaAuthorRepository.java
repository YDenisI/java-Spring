package ru.cr.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.cr.hw.models.Author;

import java.util.List;
import java.util.Optional;


@Repository
public class JpaAuthorRepository implements AuthorRepository {

    private static final String SQL_QUERY_AUTHOR_FIND_ALL = "select a from Author a";

    private static final String SQL_QUERY_AUTHOR_FIND_BY_ID = "select a from Author a where a.id = :id";

    @PersistenceContext
    private final EntityManager em;

    public JpaAuthorRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Author> findAll() {
        return em.createQuery(SQL_QUERY_AUTHOR_FIND_ALL, Author.class).getResultList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }
}
