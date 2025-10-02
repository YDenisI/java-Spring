package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookCreateDto {

    @NotBlank
    private String title;

    @NotNull
    private Long authorId;

    @NotNull
    private Long genreId;

    private CommentCreateDto initialComment;

    public BookCreateDto() {

    }

    public BookCreateDto(String title, Long authorId, Long genreId, CommentCreateDto initialComment) {
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
        this.initialComment = initialComment;
    }
}
