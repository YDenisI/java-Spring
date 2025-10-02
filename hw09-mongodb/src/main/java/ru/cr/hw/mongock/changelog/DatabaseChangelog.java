package ru.cr.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        Document author2 = new Document("_id", new ObjectId()).append("fullName", "Author_2");
        authors.insertOne(author1);
        authors.insertOne(author2);

        var genres = db.getCollection("genres");
        Document genre1 = new Document("_id", new ObjectId()).append("name", "Genre_1");
        Document genre2 = new Document("_id", new ObjectId()).append("name", "Genre_2");
        genres.insertOne(genre1);
        genres.insertOne(genre2);

        var books = db.getCollection("books");
        Document book1 = new Document("_id", new ObjectId())
                .append("title", "Title_1")
                .append("author", new Document("$ref", "authors").append("$id", author1.getObjectId("_id")))
                .append("genre", new Document("$ref", "genres").append("$id", genre1.getObjectId("_id")));
        Document book2 = new Document("_id", new ObjectId())
                .append("title", "Title_2")
                .append("author", new Document("$ref", "authors").append("$id", author2.getObjectId("_id")))
                .append("genre", new Document("$ref", "genres").append("$id", genre2.getObjectId("_id")));
        Document book3 = new Document("_id", new ObjectId())
                .append("title", "Title_3")
                .append("author", new Document("$ref", "authors").append("$id", author1.getObjectId("_id")))
                .append("genre", new Document("$ref", "genres").append("$id", genre2.getObjectId("_id")));
        books.insertOne(book1);
        books.insertOne(book2);
        books.insertOne(book3);

        var comments = db.getCollection("comments");
        Document comment1 = new Document("_id", new ObjectId())
                .append("comment", "Comment_1")
                .append("bookId", book1.getObjectId("_id").toString());
        Document comment2 = new Document("_id", new ObjectId())
                .append("comment", "Comment_2")
                .append("bookId", book2.getObjectId("_id").toString());
        Document comment3 = new Document("_id", new ObjectId())
                .append("comment", "Comment_3")
                .append("bookId", book1.getObjectId("_id").toString());

        comments.insertOne(comment1);
        comments.insertOne(comment2);
        comments.insertOne(comment3);
    }
}
