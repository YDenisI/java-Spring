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

    private static final String SQL_SELECT_BOOK_FIND_ALL = "select b from Book b " +
                                                           "join fetch b.author " +
                                                           "join fetch b.genre order by b.id";

    private static final String SQL_SELECT_BOOK_FIND_BY_ID = "select b from Book b " +
                                                             "join fetch b.author " +
                                                             "join fetch b.genre where b.id = :id";

    private static final String SQL_DELETE_BOOK = "delete from Book b where b.id = :id";


    @PersistenceContext
    private final EntityManager em;

    public JpaBookRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Optional<Book> findById(long id) {

        TypedQuery<Book> query = em.createQuery(
                SQL_SELECT_BOOK_FIND_BY_ID
                , Book.class);
        query.setParameter("id", id);
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery(SQL_SELECT_BOOK_FIND_ALL, Book.class)
                .getResultList();

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

        em.createQuery(SQL_DELETE_BOOK)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    private Book insert(Book book) {
        em.persist(book);
        em.flush();
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
