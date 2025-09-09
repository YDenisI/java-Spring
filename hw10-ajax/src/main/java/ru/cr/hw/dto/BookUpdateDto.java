package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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

        public @NotNull Long getId() {
                return id;
        }

        public void setId(@NotNull Long id) {
                this.id = id;
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
}

