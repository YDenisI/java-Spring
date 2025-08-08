package ru.cr.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import ru.cr.hw.models.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaGenreRepository implements GenreRepository {

    private static final String SQL_QUERY_GENRE_FIND_ALL = "select g from Genre g";

    private static final String SQL_QUERY_GENRE_FIND_BY_ID = "select g from Genre g where g.id = :id";

    @PersistenceContext
    private final EntityManager em;

    public JpaGenreRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Genre> findAll() {
        return em.createQuery(SQL_QUERY_GENRE_FIND_ALL, Genre.class).getResultList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }
}
