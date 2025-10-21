package ru.cr.hw.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.cr.hw.domain.Comment;
import ru.cr.hw.dto.CommentCreateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.dto.CommentUpdateDto;
import ru.cr.hw.repostory.BookRepository;
import ru.cr.hw.repostory.CommentRepository;
import ru.cr.hw.rest.exceptions.NotFoundException;


@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Mono<CommentDto> findById(String id) {
            return commentRepository.findById(id)
                    .map(CommentDto::fromDomain)
                    .switchIfEmpty(Mono.error(new NotFoundException("Comment not found!")));
    }

    @Override
    public Flux<CommentDto>  findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId)
                .map(CommentDto::fromDomain)
                .switchIfEmpty(Flux.error(new NotFoundException("Comment not found!")));
    }


    @Override
    public Flux<CommentDto> findAll() {
        return commentRepository.findAll()
                .map(CommentDto::fromDomain)
                .switchIfEmpty(Flux.error(new NotFoundException("Comment not found!")));
    }

    @Override
    public Mono<CommentDto> insert(CommentCreateDto commentCreateDto) {
        String bookId = commentCreateDto.getBookId();
        String comment = commentCreateDto.getComment();

        return bookRepository.findById(bookId)
          .switchIfEmpty(Mono.error(new NotFoundException("Book with id " + commentCreateDto.getBookId()
                  + " not found")))
          .flatMap(existBook -> {
             var newComment = new Comment(comment, existBook);
                 return commentRepository.save(newComment);

          })
          .map(CommentDto::fromDomain);
    }

    @Override
    public Mono<CommentDto> update(CommentUpdateDto commentUpdateDto) {
        String id = commentUpdateDto.getId();
        String comment = commentUpdateDto.getComment();

        return commentRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Comment with id " +
                        commentUpdateDto.getId() + " not found")))
                .flatMap(existComment -> {
                    existComment.setComment(comment);
                    return commentRepository.save(existComment);
                })
                .map(CommentDto::fromDomain);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return commentRepository.deleteById(id);

    }
}
