package ru.cr.hw.dto;

import lombok.Data;

@Data
public class BookUpdateDto {

        private String id;

        private String title;

        private String authorId;

        private String genreId;

        public BookUpdateDto() {

        }

        public BookUpdateDto(String id, String title, String authorId, String genreId) {
                this.id = id;
                this.title = title;
                this.authorId = authorId;
                this.genreId = genreId;
        }
}

