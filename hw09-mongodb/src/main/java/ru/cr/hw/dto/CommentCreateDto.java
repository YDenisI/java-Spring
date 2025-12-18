package ru.cr.hw.dto;

import lombok.Data;

@Data
public class CommentCreateDto {

    private String comment;

    private String bookId;

    public CommentCreateDto() {
    }

    public CommentCreateDto(String comment, String bookId) {
        this.comment = comment;
        this.bookId = bookId;
    }
}
