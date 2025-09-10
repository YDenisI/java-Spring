package ru.cr.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.repostory.AuthorRepository;
import ru.cr.hw.repostory.BookRepository;
import ru.cr.hw.repostory.CommentRepository;
import ru.cr.hw.repostory.GenreRepository;
import ru.cr.hw.services.AuthorService;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;
import ru.cr.hw.services.GenreService;

import java.util.List;


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

    private List<BookDto> books = List.of(
            new BookDto(1L, "Book_Title", new AuthorDto(1L, "John Doe"), new GenreDto(1L, "Fiction"), null),
            new BookDto(2L, "Book_Title_2", new AuthorDto(1L, "John Doe"), new GenreDto(1L, "Fiction"), null)
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
        BookDto book = new BookDto(1L, "Book_Title", new AuthorDto(1L, "John Doe"), new GenreDto(1L, "Fiction"), null);

        List<AuthorDto> authors = List.of(new AuthorDto(1L, "John Doe"));
        List<GenreDto> genres = List.of(new GenreDto(1L, "Fiction"));

        when(bookService.findById(1L)).thenReturn(book);
        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("isEdit", true))
                .andExpect(model().attribute("allAuthors", authors))
                .andExpect(model().attribute("allGenres", genres));
    }

    @Test
    void editPage_WithoutId_ShouldReturnEmptyForm() throws Exception {
        List<AuthorDto> authors = List.of(new AuthorDto(1L, "John Doe"));
        List<GenreDto> genres = List.of(new GenreDto(1L, "Fiction"));

        when(authorService.findAll()).thenReturn(authors);
        when(genreService.findAll()).thenReturn(genres);

        mockMvc.perform(get("/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attribute("isEdit", false))
                .andExpect(model().attribute("allAuthors", authors))
                .andExpect(model().attribute("allGenres", genres));
    }
    @Test
    void deleteBook_ExistingId_ShouldRedirect() throws Exception {
        BookDto book = new BookDto();
        book.setId(1L);

        when(bookService.findById(1L)).thenReturn(book);

        mockMvc.perform(post("/deleteBook")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).deleteById(book.getId());
    }

    @Test
    void viewPage_ShouldReturnBookDetails() throws Exception {
        BookDto bookDto = new BookDto(1L, "Book_Title", new AuthorDto(1L, "John Doe"), new GenreDto(1L, "Fiction"), null);
        List<CommentDto> comments = List.of(
                new CommentDto(1L, "Great book!", 1L),
                new CommentDto(2L, "Interesting read", 1L)
        );


        when(bookService.findById(1L)).thenReturn(bookDto);
        when(commentService.findByBookId(1L)).thenReturn(comments);

        mockMvc.perform(get("/viewbook").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("view_comments"))
                .andExpect(model().attribute("book", bookDto))
                .andExpect(model().attribute("comments", comments))
                .andExpect(model().attributeExists("newComment"))
                .andExpect(model().attribute("newComment",
                        org.hamcrest.Matchers.hasProperty("bookId", org.hamcrest.Matchers.is(1L))
                ))
                .andExpect(model().attributeDoesNotExist("allAuthors", "allGenres", "editingComment", "editingCommentId"));
    }

    @Test
    public void testViewPage_WhenServiceThrowsException_ShouldReturn500() throws Exception {
        when(bookService.findById(1L)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(get("/viewbook").param("id", "1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldReturn404() throws Exception {
        doThrow(new NotFoundException())
                .when(bookService).deleteById(999L);

        mockMvc.perform(post("/deleteBook").param("bookId", "999"))
                .andExpect(status().isNotFound());

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
