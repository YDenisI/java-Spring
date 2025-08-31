package ru.cr.hw.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cr.hw.domain.Author;
import ru.cr.hw.domain.Book;
import ru.cr.hw.domain.Genre;
import ru.cr.hw.domain.Comment;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.services.AuthorService;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;
import ru.cr.hw.services.GenreService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final AuthorService authorService;

    private final GenreService genreService;

    private final CommentService commentService;

    @GetMapping("/")
    public String listPage(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list_books";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam(value = "id", required = false) Long id, Model model) {
        List<Author> allAuthors = authorService.findAll();
        List<Genre> allGenres = genreService.findAll();
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allGenres", allGenres);

        BookDto bookDto;
        if (id != null) {
            Optional<Book> bookOpt = bookService.findById(id);
            if (bookOpt.isEmpty()) {
                throw new NotFoundException();
            }
            bookDto = BookDto.fromDomain(bookOpt.get());
        } else {
            bookDto = new BookDto();
            if (!allAuthors.isEmpty()) {
                bookDto.setAuthorId(allAuthors.get(0).getId());
            }
            if (!allGenres.isEmpty()) {
                bookDto.setGenreId(allGenres.get(0).getId());
            }
        }
        model.addAttribute("book", bookDto);
        return "add_edit";
    }

    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute("book") BookDto bookDto,
                           BindingResult bindingResult,
                            // @RequestParam("author.id") Long authorId,
                            // @RequestParam("genre.id") Long genreId,
                             @RequestParam(value = "initialComment", required = false) String initialComment,
                             Model model) {

        if (bindingResult.hasErrors()) {
            List<Author> allAuthors = authorService.findAll();
            List<Genre> allGenres = genreService.findAll();
            model.addAttribute("allAuthors", allAuthors);
            model.addAttribute("allGenres", allGenres);
            return "add_edit";
        }

        Author author = authorService.findById(bookDto.getAuthorId()).orElseThrow(() -> new NotFoundException());
        Genre genre = genreService.findById(bookDto.getGenreId()).orElseThrow(() -> new NotFoundException());

        bookService.update(bookDto.getId(), bookDto.getTitle(), author.getId(), genre.getId());

   /*     Optional<Author> authorOpt = authorService.findById(authorId);
        Optional<Genre> genreOpt = genreService.findById(genreId);
        Book existingBook = bookService.findById(book.getId()).orElseThrow();
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(authorOpt.get());
        existingBook.setGenre(genreOpt.get());
        bookService.update(book.getId(),book.getTitle(), authorOpt.get().getId(), genreOpt.get().getId());
*/
        return "redirect:/";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") BookDto bookDto,
                           @RequestParam("authorId") Long authorId,
                           @RequestParam("genreId") Long genreId,
                           @RequestParam(value = "initialComment", required = false) String initialComment,
                           Model model) {

        Optional<Author> authorOpt = authorService.findById(authorId);
        Optional<Genre> genreOpt = genreService.findById(genreId);
        Book book =  bookDto.toDomain(authorOpt.get(), genreOpt.get());
        Book savedBook = bookService.insert(book.getTitle(), book.getAuthor().getId(), book.getGenre().getId());
            if (initialComment != null && !initialComment.trim().isEmpty()) {
                Comment comment = new Comment();
                comment.setComment(initialComment.trim());
                comment.setBook(savedBook);
                commentService.insert(initialComment.trim(), savedBook.getId());
            }
        return "redirect:/";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam("bookId") Long id, RedirectAttributes redirectAttributes) {
        Optional<Book> bookOpt = bookService.findById(id);
        if (bookOpt.isEmpty()) {
            throw new NotFoundException();
        }
        if (bookOpt.isPresent()) {
            bookService.deleteById(bookOpt.get().getId());
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

        Optional<Book> book = bookService.findById(id);
        if (book.isEmpty()) {
            throw new NotFoundException();
        }
        List<Author> allAuthors = authorService.findAll();
        List<Genre> allGenres = genreService.findAll();
        List<Comment> comments = commentService.findByBookId(book.get().getId());
        model.addAttribute("book", book.get());
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allGenres", allGenres);
        model.addAttribute("comments", comments);

        if (editCommentId != null) {
            model.addAttribute("editingCommentId", editCommentId);
        }

        return "view_comments";
    }
}
