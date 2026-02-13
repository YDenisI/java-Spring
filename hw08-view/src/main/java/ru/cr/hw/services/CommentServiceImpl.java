package ru.cr.hw.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.cr.hw.controller.NotFoundException;
import ru.cr.hw.domain.Comment;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.repostory.BookRepository;
import ru.cr.hw.repostory.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Override
    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        return CommentDto.fromDomain(comment);
    }

    @Override
    public List<CommentDto>  findByBookId(Long bookId) {
        List<Comment> comments = commentRepository.findByBookId(bookId);
        return comments.stream()
                .map(CommentDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto insert(String comment, long bookId) {
        var existBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException());
        var newComment = new Comment(comment, existBook);
        return CommentDto.fromDomain(commentRepository.save(newComment));
    }

    @Override
    @Transactional
    public CommentDto  update(long id, String comment) {
        var existComment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        existComment.setComment(comment);
        return CommentDto.fromDomain(commentRepository.save(existComment));
    }

    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
