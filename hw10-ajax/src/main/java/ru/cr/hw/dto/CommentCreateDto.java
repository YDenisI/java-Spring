package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentCreateDto {

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 5, max = 500, message = "Комментарий должен быть от 5 до 500 символов")
    private String comment;

    @NotNull
    private Long bookId;

    public CommentCreateDto() {
    }

    public CommentCreateDto(String comment, Long bookId) {
        this.comment = comment;
        this.bookId = bookId;
    }
}
