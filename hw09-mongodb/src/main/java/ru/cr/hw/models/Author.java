package ru.cr.hw.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "authors")
public class Author {

    @Id
    private String id;

    private String fullName;

    public Author() {

    }

    public Author(String fullName) {
        this.fullName = fullName;
    }

    public Author(String id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
}
