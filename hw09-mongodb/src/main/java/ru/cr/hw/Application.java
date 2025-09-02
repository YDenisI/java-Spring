package ru.cr.hw;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.cr.hw.models.Author;
import ru.cr.hw.models.Book;
import ru.cr.hw.models.Comment;
import ru.cr.hw.models.Genre;
import ru.cr.hw.repositories.AuthorRepository;
import ru.cr.hw.repositories.BookRepository;
import ru.cr.hw.repositories.CommentRepository;
import ru.cr.hw.repositories.GenreRepository;
import ru.cr.hw.services.AuthorService;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;
import ru.cr.hw.services.GenreService;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = SpringApplication.run(Application.class);

        // Получение репозиториев из контекста
        AuthorService authorService = context.getBean(AuthorService.class);
        BookService bookService = context.getBean(BookService.class);
        GenreService genreService = context.getBean(GenreService.class);
        CommentService commentService = context.getBean(CommentService.class);

        Author author1 = authorService.insert(new Author("Лев Толстой"));
        Author author2 = authorService.insert(new Author("Фёдор Достоевский"));
        Author author3 = authorService.insert(new Author("Антон Чехов"));

        Genre genre1 = genreService.insert(new Genre("Роман"));
        Genre genre2 = genreService.insert(new Genre("Драма"));
        Genre genre3 = genreService.insert(new Genre("Рассказ"));

        Book book1 = bookService.insert("Война и мир", author1.getId(), genre2.getId());
        Book book2 = bookService.insert("Анна Каренина", author1.getId(), genre1.getId());
        Book book3 = bookService.insert("Преступление и наказание", author2.getId(), genre1.getId());
        Book book4 = bookService.insert("Вишневый сад", author3.getId(), genre2.getId());

        // Создание комментариев
        commentService.insert("Отличная книга!", book1.getId());
        commentService.insert("Очень понравилось", book1.getId());
        commentService.insert("Классика русской литературы", book2.getId());
        commentService.insert("Глубокое произведение", book3.getId());
        commentService.insert("Интересная пьеса", book4.getId());

        System.out.println("Данные успешно загружены в MongoDB");
    }
}
