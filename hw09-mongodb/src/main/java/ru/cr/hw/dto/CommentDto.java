package ru.cr.hw.dto;

import lombok.Data;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Comment;

@Data
public class CommentDto {

    private String id;

    private String comment;

    private String bookId;

    public CommentDto() {
    }

    public CommentDto(String id, String comment, String bookId) {
        this.id = id;
        this.comment = comment;
        this.bookId = bookId;
    }

    public static CommentDto fromDomain(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                comment.getBook().getId()
                );
    }

    public Comment toDomain(Book book) {
        Comment c = new Comment();
        if (id != null) {
            c.setId(id);
        }
        c.setComment(comment);
        c.setBook(book);
        return c;
    }
}
