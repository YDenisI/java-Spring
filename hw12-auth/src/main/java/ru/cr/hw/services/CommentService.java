package ru.cr.hw.services;

import ru.cr.hw.dto.CommentCreateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.dto.CommentUpdateDto;

import java.util.List;

public interface CommentService {

    CommentDto findById(Long id);

    List<CommentDto> findByBookId(Long bookId);

    CommentDto insert(CommentCreateDto commentCreateDto);

    CommentDto update(CommentUpdateDto commentUpdateDto);

    void deleteById(long id);
}
