package ru.cr.hw.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cr.hw.domain.Author;
import ru.cr.hw.domain.Book;
import ru.cr.hw.domain.Genre;
import ru.cr.hw.domain.Comment;
import ru.cr.hw.repostory.AuthorRepository;
import ru.cr.hw.repostory.BookRepository;
import ru.cr.hw.repostory.CommentRepository;
import ru.cr.hw.repostory.GenreRepository;


import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    @GetMapping("/")
    public String listPage(Model model) {
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        return "list_books";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam(value = "id", required = false) Long id, Model model) {
        List<Author> allAuthors = authorRepository.findAll();
        List<Genre> allGenres = genreRepository.findAll();

           model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allGenres", allGenres);

        Book book;
        if (id != null) {
            book = bookRepository.findById(id).orElse(new Book());
        } else {

            book = new Book();
            if (!allAuthors.isEmpty()) {
                book.setAuthor(allAuthors.get(0));
            }
            if (!allGenres.isEmpty()) {
                book.setGenre(allGenres.get(0));
            }
        }
        model.addAttribute("book", book);
        return "add_edit";
    }

    @PostMapping("/edit")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                             @RequestParam("author.id") Long authorId,
                             @RequestParam("genre.id") Long genreId,
                             @RequestParam(value = "initialComment", required = false) String initialComment,
                             Model model) {

        Optional<Author> authorOpt = authorRepository.findById(authorId);
        Optional<Genre> genreOpt = genreRepository.findById(genreId);
        Book savedBook;
        if (book.getId() == 0) {
            book.setAuthor(authorOpt.get());
            book.setGenre(genreOpt.get());
            savedBook = bookRepository.save(book);
            if (initialComment != null && !initialComment.trim().isEmpty()) {
                Comment comment = new Comment();
                comment.setComment(initialComment.trim());
                comment.setBook(savedBook);
                commentRepository.save(comment);
            }
        } else {
            Book existingBook = bookRepository.findById(book.getId()).orElseThrow();
            existingBook.setTitle(book.getTitle());
            existingBook.setAuthor(authorOpt.get());
            existingBook.setGenre(genreOpt.get());
            bookRepository.save(existingBook);
        }
        return "redirect:/";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam("bookId") Long id, RedirectAttributes redirectAttributes) {
        System.out.println("BINGO");
        Optional<Book> bookOpt = bookRepository.findById(id);
        if (bookOpt.isPresent()) {
            bookRepository.delete(bookOpt.get());
            redirectAttributes.addFlashAttribute("message", "Book deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Book not found!");
        }
        return "redirect:/";
    }

    @GetMapping("/viewbook")
    public String viewPage(@RequestParam("id") long id,
                           @RequestParam(value = "editCommentId", required = false) Long editCommentId,
                           Model model) {

        Optional<Book> book = bookRepository.findById(id);
        List<Author> allAuthors = authorRepository.findAll();
        List<Genre> allGenres = genreRepository.findAll();
        List<Comment> comments = commentRepository.findByBookId(book.get().getId());
        model.addAttribute("book", book.get());
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allGenres", allGenres);
        model.addAttribute("comments", comments);

        if (editCommentId != null) {
            model.addAttribute("editingCommentId", editCommentId);
        }

        return "view_comments";
    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam("bookId") Long bookId,
                             @RequestParam("commentText") String commentText,
                             RedirectAttributes redirectAttributes) {

        Optional<Book> book = bookRepository.findById(bookId);
        if (book.isPresent()) {
            Comment comment = new Comment();
            comment.setComment(commentText);
            comment.setBook(book.get());
            commentRepository.save(comment);
        }

        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/viewbook";
    }

    @PostMapping("/editComment")
    public String editComment(@RequestParam("commentId") Long commentId,
                              @RequestParam("commentText") String commentText,
                              RedirectAttributes redirectAttributes) {

        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            comment.setComment(commentText);
            commentRepository.save(comment);
            redirectAttributes.addAttribute("id", comment.getBook().getId());
        }

        return "redirect:/viewbook";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                RedirectAttributes redirectAttributes) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isPresent()) {
            Long bookId = commentOpt.get().getBook().getId();
            commentRepository.deleteById(commentId);
            redirectAttributes.addAttribute("id", bookId);
        }

        return "redirect:/viewbook";
    }
}
