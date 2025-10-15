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
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.dto.BookCreateDto;
import ru.cr.hw.dto.BookUpdateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.services.AuthorService;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;
import ru.cr.hw.services.GenreService;

import java.util.List;

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
        List<BookDto> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list_books";
    }

    @GetMapping("/book/add")
    public String addBook(@RequestParam(value = "id", required = false) Long id, Model model) {
        List<AuthorDto> allAuthors = authorService.findAll();
        List<GenreDto> allGenres = genreService.findAll();
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allGenres", allGenres);


        Long defaultAuthorId = allAuthors.isEmpty() ? null : allAuthors.get(0).getId();
        Long defaultGenreId = allGenres.isEmpty() ? null : allGenres.get(0).getId();
        BookCreateDto bookCreateDto = new BookCreateDto(null, defaultAuthorId, defaultGenreId, null);
        model.addAttribute("book", bookCreateDto);
        model.addAttribute("isEdit", false);
        return "add_edit";
    }

    @GetMapping("/book/edit")
    public String editBook(@RequestParam(value = "id", required = false) Long id, Model model) {
        List<AuthorDto> allAuthors = authorService.findAll();
        List<GenreDto> allGenres = genreService.findAll();
        model.addAttribute("allAuthors", allAuthors);
        model.addAttribute("allGenres", allGenres);

        BookDto existingBook = bookService.findById(id);
            if (existingBook != null) {
                BookUpdateDto bookUpdateDto = new BookUpdateDto(existingBook.getId(), existingBook.getTitle(),
                        existingBook.getAuthor().getId(), existingBook.getGenre().getId());
                model.addAttribute("book", bookUpdateDto);
                model.addAttribute("isEdit", true);
            } else {
                return "redirect:/";
            }

        return "add_edit";
    }

    @PostMapping("/book/edit")
    public String updateBook(@Valid @ModelAttribute("book") BookUpdateDto bookUpdateDto,
                             BindingResult bindingResult,
                             @RequestParam(value = "initialComment", required = false) String initialComment,
                             Model model) {

        if (bindingResult.hasErrors()) {
            List<AuthorDto> allAuthors = authorService.findAll();
            List<GenreDto> allGenres = genreService.findAll();
            model.addAttribute("allAuthors", allAuthors);
            model.addAttribute("allGenres", allGenres);
            return "add_edit";
        }

        bookService.update(bookUpdateDto);

        return "redirect:/";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute("book") BookCreateDto bookCreateDto,
                           @RequestParam(value = "initialComment", required = false) String initialComment,
                           Model model) {

        BookDto savedBook = bookService.insert(bookCreateDto);
        return "redirect:/";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam("bookId") Long id, RedirectAttributes redirectAttributes) {

         bookService.deleteById(id);
         redirectAttributes.addFlashAttribute("message", "Book deleted successfully!");

        return "redirect:/";
    }

    @GetMapping("/viewbook")
    public String viewPage(@RequestParam("id") long id,
                           @RequestParam(value = "editCommentId", required = false) Long editCommentId,
                           Model model) {

        BookDto book = bookService.findById(id);
        List<CommentDto> comments = commentService.findByBookId(book.getId());
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);

        CommentDto newComment = new CommentDto(null, "", book.getId());
        model.addAttribute("newComment", newComment);

        if (editCommentId != null) {
            CommentDto editingComment = commentService.findById(editCommentId);
            model.addAttribute("editingComment", editingComment);
            model.addAttribute("editingCommentId", editCommentId);
        }

        return "view_comments";
    }
}
