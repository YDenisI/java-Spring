package ru.cr.hw.services;

import ru.cr.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(Long id);

    List<Comment> findByBookId(Long bookId);

    Comment insert(String comment, long bookId);

    Comment update(long id, String comment);

    void deleteById(long id);
}
