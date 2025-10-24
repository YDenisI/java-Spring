package ru.cr.hw.rest.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.cr.hw.dto.*;
import ru.cr.hw.rest.BookRestController;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookRestController.class)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CommentService commentService;


    private BookDto createBookDto(Long id, String title, Long authorId, String authorName, Long genreId, String genreName) {
        AuthorDto author = new AuthorDto(authorId, authorName);
        GenreDto genre = new GenreDto(genreId, genreName);
        return new BookDto(id, title, author, genre, null);
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        List<BookDto> books = List.of(
                createBookDto(1L, "Book One", 1L, "Author One", 1L, "Genre One"),
                createBookDto(2L, "Book Two", 2L, "Author Two", 2L, "Genre Two")
        );

        given(bookService.findAll()).willReturn(books);

        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(books)));
    }

    @Test
    void shouldReturnEmptyListWhenNoBooks() throws Exception {
        given(bookService.findAll()).willReturn(List.of());

        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        BookDto book = createBookDto(1L, "Book One", 1L, "Author One", 1L, "Genre One");

        given(bookService.findById(1L)).willReturn(book);

        mockMvc.perform(get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(book)));
    }

    @Test
    void shouldCreateBookWithoutInitialComment() throws Exception {
        BookCreateDto createDto = new BookCreateDto("New Book", 1L, 2L, new CommentCreateDto("New comment", null));
        BookDto savedBook = createBookDto(3L, "New Book", 1L, "Author One", 2L, "Genre Two");

        given(bookService.insert(createDto)).willReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedBook)));
    }

    @Test
    void shouldCreateBookWithInitialComment() throws Exception {
        BookCreateDto createDto = new BookCreateDto("New Book", 1L, 2L, new CommentCreateDto("New comment", null));
        BookDto savedBook = createBookDto(3L, "New Book", 1L, "Author One", 2L, "Genre Two");

        given(bookService.insert(createDto)).willReturn(savedBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(savedBook)));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookUpdateDto updateDto = new BookUpdateDto(1L, "Updated Book", 1L, 2L);
        BookDto updatedBook = createBookDto(1L, "Updated Book", 1L, "Author One", 2L, "Genre Two");

        given(bookService.findById(1L)).willReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedBook)));
    }

    @Test
    void shouldThrowExceptionWhenIdsDoNotMatch() throws Exception {
        BookUpdateDto updateDto = new BookUpdateDto(2L, "Updated Book", 1L, 2L);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingWithInvalidData() throws Exception {
        BookUpdateDto invalidDto = new BookUpdateDto(1L, "", 1L, 2L);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}