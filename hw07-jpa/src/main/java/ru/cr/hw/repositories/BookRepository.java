package ru.cr.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.cr.hw.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b join fetch b.author join fetch b.genre order by b.id")
    List<Book> findAll();

    @Query("select b from Book b join fetch b.author join fetch b.genre where b.id = :id")
    Optional<Book> findById(@Param("id") Long id);
}
