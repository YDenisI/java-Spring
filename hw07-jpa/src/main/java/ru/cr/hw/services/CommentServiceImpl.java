package ru.cr.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Comment;
import ru.cr.hw.repositories.BookRepository;
import ru.cr.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment>  findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }


    @Override
    public Comment insert(String comment, long bookId) {
        var existBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var newComment = new Comment(comment, existBook);
        return commentRepository.save(newComment);
    }

    @Override
    @Transactional
    public Comment update(long id, String comment) {
        var existComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        existComment.setComment(comment);
        return commentRepository.save(existComment);
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
