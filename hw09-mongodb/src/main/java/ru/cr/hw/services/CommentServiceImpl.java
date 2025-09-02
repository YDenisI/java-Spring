package ru.cr.hw.services;

import org.springframework.stereotype.Service;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Comment;
import ru.cr.hw.repositories.BookRepository;
import ru.cr.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment>  findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }


    @Override
    public Comment insert(String comment, String bookId) {
        var existBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var newComment = new Comment(comment, existBook);
        return commentRepository.save(newComment);
    }

    @Override
    public Comment update(String id, String comment) {
        var existComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        existComment.setComment(comment);
        return commentRepository.save(existComment);
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
