package ru.cr.hw.dto;

import lombok.Data;

@Data
public class CommentUpdateDto {

    private String id;

    private String comment;

    public CommentUpdateDto() {
    }

    public CommentUpdateDto(String id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
