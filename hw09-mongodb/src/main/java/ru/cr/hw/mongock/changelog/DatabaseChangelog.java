package ru.cr.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

@ChangeLog(order = "001")
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "createCollections", author = "ydi")
    public void createCollections(MongoDatabase db) {
        db.createCollection("authors");
        db.createCollection("genres");
        db.createCollection("books");
        db.createCollection("comments");
    }

    @ChangeSet(order = "002", id = "insertInitialData", author = "ydi")
    public void insertInitialData(MongoDatabase db) {
        var authors = db.getCollection("authors");
        Document author1 = new Document("_id", new ObjectId()).append("fullName", "Author_1");
        authors.insertOne(author1);
        var genres = db.getCollection("genres");
        Document genre1 = new Document("_id", new ObjectId()).append("name", "Genre_1");
        genres.insertOne(genre1);

        var books = List.of(
          new Document("_id", new ObjectId()).append("title", "Title_1")
                  .append("author", new Document("$ref", "authors").append("$id", author1.getObjectId("_id")))
                  .append("genre", new Document("$ref", "genres").append("$id", genre1.getObjectId("_id"))),
          new Document("_id", new ObjectId()).append("title", "Title_2")
                 .append("author", new Document("$ref", "authors").append("$id", author1.getObjectId("_id")))
                 .append("genre", new Document("$ref", "genres").append("$id", genre1.getObjectId("_id")))
        );
        db.getCollection("books").insertMany(books);
        var comments = List.of(
                new Document("_id", new ObjectId()).append("comment", "Comment_1")
                        .append("book", new Document("$ref", "books").append("$id", books.get(0).getObjectId("_id"))),
                new Document("_id", new ObjectId()).append("comment", "Comment_2")
                        .append("book", new Document("$ref", "books").append("$id", books.get(1).getObjectId("_id")))
        );
        db.getCollection("comments").insertMany(comments);
    }
}
