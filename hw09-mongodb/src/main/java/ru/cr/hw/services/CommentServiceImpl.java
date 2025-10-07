package ru.cr.hw.services;

import org.springframework.stereotype.Service;
import ru.cr.hw.dto.CommentCreateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.dto.CommentUpdateDto;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Comment;
import ru.cr.hw.repositories.BookRepository;
import ru.cr.hw.repositories.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public CommentDto findById(String id) {
            Comment comment = commentRepository.findById(id).
                    orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
            return CommentDto.fromDomain(comment);
    }

    @Override
    public List<CommentDto>  findByBookId(String bookId) {
        List<Comment> comments = commentRepository.findByBookId(bookId);

        return comments.stream()
                .map(CommentDto::fromDomain)
                .collect(Collectors.toList());
    }


    @Override
    public List<CommentDto> findAll() {
        List<CommentDto> commentDtos = commentRepository.findAll().stream()
                .map(CommentDto::fromDomain)
                .collect(Collectors.toList());
        if (commentDtos.isEmpty()) {
            throw new EntityNotFoundException("Comments not found");
        }
        return commentDtos;
    }

    @Override
    public CommentDto insert(CommentCreateDto commentCreateDto) {
        String bookId = commentCreateDto.getBookId();
        String comment = commentCreateDto.getComment();
        var existBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id %s not found".formatted(bookId)));
        var newComment = new Comment(comment, existBook);
        return CommentDto.fromDomain(commentRepository.save(newComment));
    }

    @Override
    public CommentDto update(CommentUpdateDto commentUpdateDto) {
        String id = commentUpdateDto.getId();
        String comment = commentUpdateDto.getComment();
        var existComment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id %s not found".formatted(id)));
        existComment.setComment(comment);
        return CommentDto.fromDomain(commentRepository.save(existComment));
    }

    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
