package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentUpdateDto {

    @NotNull
    private Long id;

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 5, max = 500, message = "Комментарий должен быть от 5 до 500 символов")
    private String comment;

    public CommentUpdateDto() {
    }

    public CommentUpdateDto(Long id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
