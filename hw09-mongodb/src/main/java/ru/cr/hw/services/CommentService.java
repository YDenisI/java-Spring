package ru.cr.hw.services;

import ru.cr.hw.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<Comment> findById(String id);

    List<Comment> findByBookId(String bookId);

    Comment insert(String comment, String bookId);

    Comment update(String id, String comment);

    void deleteById(String id);
}
