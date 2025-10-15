package ru.cr.hw.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.security.SecurityConfiguration;
import ru.cr.hw.services.*;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfiguration.class)
public class BookControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;
    @MockitoBean
    private AuthorService authorService;
    @MockitoBean
    private GenreService genreService;
    @MockitoBean
    private CommentService commentService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    public void whenNotAuthenticated_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void whenAuthenticated_thenAccessGranted() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenNotAuthenticated_accessAddBookRedirects() throws Exception {
        mockMvc.perform(get("/book/add"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void whenAuthenticated_accessAddBookOk() throws Exception {
        mockMvc.perform(get("/book/add"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenNotAuthenticated_accessEditBookRedirects() throws Exception {
        mockMvc.perform(get("/book/edit?id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void whenAuthenticated_accessEditBookOk() throws Exception {
        AuthorDto author = new AuthorDto(1L, "Author Name");
        GenreDto genre = new GenreDto(1L, "Genre Name");
        BookDto book = new BookDto(1L, "Book Title", author, genre, null);

        when(bookService.findById(1L)).thenReturn(book);
        when(authorService.findAll()).thenReturn(List.of(author));
        when(genreService.findAll()).thenReturn(List.of(genre));

        mockMvc.perform(get("/book/edit").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenNotAuthenticated_postCreateRedirects() throws Exception {
        mockMvc.perform(post("/create"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void whenAuthenticated_postCreateOk() throws Exception {
        mockMvc.perform(post("/create")
                        .param("title", "Test Book")
                        .param("authorId", "1")
                        .param("genreId", "1")
                )
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void whenNotAuthenticated_postDeleteRedirects() throws Exception {
        mockMvc.perform(post("/deleteBook").param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void whenAuthenticated_postDeleteOk() throws Exception {
        mockMvc.perform(post("/deleteBook").param("bookId", "1"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void whenNotAuthenticated_accessViewBookRedirects() throws Exception {
        mockMvc.perform(get("/viewbook?id=1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser
    public void whenAuthenticated_accessViewBookOk() throws Exception {
        AuthorDto author = new AuthorDto(1L, "Author Name");
        GenreDto genre = new GenreDto(1L, "Genre Name");
        BookDto book = new BookDto(1L, "Book Title", author, genre, null);

        when(bookService.findById(1L)).thenReturn(book);
        mockMvc.perform(get("/viewbook?id=1"))
                .andExpect(status().isOk());
    }
}
