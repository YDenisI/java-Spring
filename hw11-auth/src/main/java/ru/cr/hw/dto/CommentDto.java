package ru.cr.hw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.cr.hw.domain.Book;
import ru.cr.hw.domain.Comment;

@Data
public class CommentDto {
    private Long id;

    @NotBlank(message = "Комментарий не может быть пустым")
    @Size(min = 5, max = 500, message = "Комментарий должен быть от 5 до 500 символов")
    private String comment;

    private Long bookId;

    public CommentDto() {
    }

    public CommentDto(Long id, String comment, Long bookId) {
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
        if (id != null && id != 0) {
            c.setId(id);
        }
        c.setComment(comment);
        c.setBook(book);
        return c;
    }
}
