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
import ru.cr.hw.models.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager em;

    public JpaBookRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Book> findById(long id) {

        TypedQuery<Book> query = em.createQuery(
                "SELECT c FROM Book c WHERE c.id = :id", Book.class);
        query.setHint("javax.persistence.fetchgraph", em.getEntityGraph("book-entity-graph"));
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery(
                "SELECT c FROM Book c", Book.class);
        query.setHint("javax.persistence.fetchgraph", em.getEntityGraph("book-entity-graph"));
        return query.getResultList();

    }

    @Override
    @Transactional
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        Book book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }

    @Transactional
    private Book insert(Book book) {
        em.persist(book);
        return book;
    }

    @Transactional
    private Book update(Book book) {

        Book managedBook = em.find(Book.class, book.getId());
        if (managedBook == null) {
            throw new EntityNotFoundException("Book with id " + book.getId() + " not found");
        }

        managedBook.setTitle(book.getTitle());

        Author managedAuthor = em.find(Author.class, book.getAuthor().getId());
        if (managedAuthor == null) {
            throw new EntityNotFoundException("Author with id " + book.getAuthor().getId() + " not found");
        }
        managedBook.setAuthor(managedAuthor);

        Genre managedGenre = em.find(Genre.class, book.getGenre().getId());
        if (managedGenre == null) {
            throw new EntityNotFoundException("Genre with id " + book.getGenre().getId() + " not found");
        }
        managedBook.setGenre(managedGenre);

        return managedBook;
    }
}
