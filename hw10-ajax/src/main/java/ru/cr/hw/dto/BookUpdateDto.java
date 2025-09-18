package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookUpdateDto {

        @NotNull
        private Long id;

        @NotBlank
        private String title;

        @NotNull
        private Long authorId;

        @NotNull
        private Long genreId;

        public BookUpdateDto() {

        }

        public BookUpdateDto(Long id, String title, Long authorId, Long genreId) {
                this.id = id;
                this.title = title;
                this.authorId = authorId;
                this.genreId = genreId;
        }
}

