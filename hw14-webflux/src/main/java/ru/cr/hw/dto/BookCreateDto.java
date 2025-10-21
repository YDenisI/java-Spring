package ru.cr.hw.dto;

import lombok.Data;

@Data
public class BookCreateDto {

        private String title;

        private String authorId;

        private String genreId;

        private String initialComment;

    public BookCreateDto() {

    }

    public BookCreateDto(String title, String authorId, String genreId, String newComment) {
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
        this.initialComment = newComment;
    }
}
