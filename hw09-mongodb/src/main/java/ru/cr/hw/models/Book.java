package ru.cr.hw.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String title;

    private Author author;

    private Genre genre;

    public Book() {

    }

    public Book(String title, Author author, Genre genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;

    }
}
