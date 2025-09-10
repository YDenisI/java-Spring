package ru.cr.hw.services;


import ru.cr.hw.dto.CommentDto;
import java.util.List;

public interface CommentService {

    CommentDto findById(Long id);

    List<CommentDto> findByBookId(Long bookId);

    CommentDto insert(String comment, long bookId);

    CommentDto update(long id, String comment);

    void deleteById(long id);
}
