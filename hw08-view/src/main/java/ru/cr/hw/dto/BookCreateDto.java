package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BookCreateDto {
    @NotBlank
    private String title;

    @NotNull
    private Long authorId;

    @NotNull
    private Long genreId;

    private String initialComment;

    public BookCreateDto() {

    }

    public BookCreateDto(String title, Long authorId, Long genreId, String initialComment) {
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
        this.initialComment = initialComment;
    }

    public @NotBlank String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank String title) {
        this.title = title;
    }

    public @NotNull Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(@NotNull Long authorId) {
        this.authorId = authorId;
    }

    public @NotNull Long getGenreId() {
        return genreId;
    }

    public void setGenreId(@NotNull Long genreId) {
        this.genreId = genreId;
    }

    public String getInitialComment() {
        return initialComment;
    }

    public void setInitialComment(String initialComment) {
        this.initialComment = initialComment;
    }
}
