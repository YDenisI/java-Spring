package ru.cr.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.cr.hw.controller.BookController;
import ru.cr.hw.domain.Author;
import ru.cr.hw.domain.Book;
import ru.cr.hw.domain.Comment;
import ru.cr.hw.domain.Genre;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.repostory.AuthorRepository;
import ru.cr.hw.repostory.BookRepository;
import ru.cr.hw.repostory.CommentRepository;
import ru.cr.hw.repostory.GenreRepository;
import ru.cr.hw.services.AuthorService;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;
import ru.cr.hw.services.GenreService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private GenreService genreService;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private AuthorRepository authorRepository;

    @MockitoBean
    private GenreRepository genreRepository;

    @MockitoBean
    private CommentRepository commentRepository;

    private List<Book> books = List.of(
            new Book(1L, "Book_Title", new Author(1L, "John Doe"), new Genre(1L, "Fiction")),
            new Book(2L, "Book_Title_2", new Author(1L, "John Doe"), new Genre(1L, "Fiction"))
    );

    @Test
    void listPage_ShouldReturnBooksList() throws Exception {
        when(bookService.findAll()).thenReturn(books);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("list_books"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    void editPage_WithId_ShouldReturnEditForm() throws Exception {
        Book book = new Book(1L, "Book_Title", new Author(1L, "John Doe"), new Genre(1L, "Fiction"));

        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(authorService.findAll()).thenReturn(Collections.singletonList(new Author()));
        when(genreService.findAll()).thenReturn(Collections.singletonList(new Genre()));

        mockMvc.perform(get("/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("allAuthors"))
                .andExpect(model().attributeExists("allGenres"));
    }

    @Test
    void editPage_WithoutId_ShouldReturnEmptyForm() throws Exception {
        when(authorService.findAll()).thenReturn(Collections.singletonList(new Author()));
        when(genreService.findAll()).thenReturn(Collections.singletonList(new Genre()));

        mockMvc.perform(get("/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("allAuthors"))
                .andExpect(model().attributeExists("allGenres"));
    }
/*
    @Test
    void saveBook_ValidData_ShouldRedirect() throws Exception {
        Author author = new Author();
        author.setId(1L);
        Genre genre = new Genre();
        genre.setId(1L);
        Book savedBook = new Book();
        savedBook.setId(1L);

        when(authorService.findById(1L)).thenReturn(Optional.of(author));
        when(genreService.findById(1L)).thenReturn(Optional.of(genre));
        when(bookService.insert(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/edit")
                        .param("title", "Test Book")
                        .param("author.id", "1")
                        .param("genre.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }*/

    @Test
    void deleteBook_ExistingId_ShouldRedirect() throws Exception {
        Book book = new Book();
        book.setId(1L);

        when(bookService.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(post("/deleteBook")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).deleteById(book.getId());
    }

    @Test
    void viewPage_ShouldReturnBookDetails() throws Exception {
        Book book = new Book(1L, "Book_Title", new Author(1L, "John Doe"), new Genre(1L, "Fiction"));

        Author author = new Author();
        author.setId(1L);
        Genre genre = new Genre();
        genre.setId(1L);
        Book savedBook = new Book();
        savedBook.setId(1L);

        when(bookService.findById(1L)).thenReturn(Optional.of(book));
        when(authorService.findAll()).thenReturn(Collections.singletonList(new Author()));
        when(genreService.findAll()).thenReturn(Collections.singletonList(new Genre()));
        when(commentService.findByBookId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/viewbook").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("view_comments"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("allAuthors"))
                .andExpect(model().attributeExists("allGenres"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    public void testViewPage_WhenServiceThrowsException_ShouldReturn500() throws Exception {
        when(bookService.findById(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/viewbook").param("id", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldReturn404() throws Exception {
        when(bookService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/deleteBook").param("bookId", "999"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("customError"))
                .andExpect(model().attributeExists("errorText"));
    }

    @Test
    void editBook_WhenTitleIsBlank_ShouldReturnFormWithErrors() throws Exception {
        mockMvc.perform(post("/edit")
                        .param("id", "1")
                        .param("title", "")
                        .param("authorId", "1")
                        .param("genreId", "1")
                        .param("initialComment", "Test comment"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_edit"))
                .andExpect(model().attributeHasFieldErrors("book", "title"))
                .andExpect(model().attributeExists("allAuthors"))
                .andExpect(model().attributeExists("allGenres"));
    }
}
