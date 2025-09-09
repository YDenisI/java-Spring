package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentCreateUpdateDto {

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 5, max = 500, message = "Комментарий должен быть от 5 до 500 символов")
    private String comment;

    public CommentCreateUpdateDto() {
    }

    public CommentCreateUpdateDto(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
