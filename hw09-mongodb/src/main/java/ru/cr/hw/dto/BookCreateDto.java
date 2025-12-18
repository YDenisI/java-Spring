package ru.cr.hw.dto;

import lombok.Data;

@Data
public class BookCreateDto {

        private String title;

        private String authorId;

        private String genreId;

    public BookCreateDto() {

    }

    public BookCreateDto(String title, String authorId, String genreId) {
        this.title = title;
        this.authorId = authorId;
        this.genreId = genreId;
    }
}
