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
        return save(0, comment, bookId);
    }

    @Override
    @Transactional
    public Comment update(long id, String comment, long bookId) {
        return save(id, comment, bookId);
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String comment, long bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %d not found".formatted(bookId)));
        var comm = new Comment(id, comment, book);
        return commentRepository.save(comm);
    }
}
