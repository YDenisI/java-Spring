package ru.cr.hw.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(value = "book-entity-graph")
    List<Book> findAll();

    @EntityGraph(value = "book-entity-graph")
    Optional<Book> findById(@Param("id") Long id);

    @EntityGraph(value = "book-entity-graph")
    List<Book> findByAuthor(Author author);
}
