package ru.cr.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.cr.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c join fetch c.book b join fetch b.author join fetch b.genre where c.id = :id")
    Optional<Comment> findById(@Param("id") long id);

    @Query("select c from Comment c join fetch c.book b join fetch b.author join fetch b.genre where b.id = :bookId")
    List<Comment> findByBookId(@Param("bookId") long bookId);
}
