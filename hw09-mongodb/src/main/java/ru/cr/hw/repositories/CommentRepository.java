package ru.cr.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.cr.hw.models.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    @Query("{ 'book.id' : ?0 }")
    List<Comment> findByBookId(@Param("bookId") String bookId);
}
