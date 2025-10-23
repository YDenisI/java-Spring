package ru.cr.hw.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    private String comment;

    @DBRef(lazy = false)
    private Book book;

    public Comment() {

    }

    public Comment(String comment, Book book) {
        this.comment = comment;
        this.book = book;
    }
}