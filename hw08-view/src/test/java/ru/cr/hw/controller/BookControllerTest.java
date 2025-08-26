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
import ru.cr.hw.repostory.AuthorRepository;
import ru.cr.hw.repostory.BookRepository;
import ru.cr.hw.repostory.CommentRepository;
import ru.cr.hw.repostory.GenreRepository;

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
        when(bookRepository.findAll()).thenReturn(books);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("list_books"))
                .andExpect(model().attribute("books", books));
    }

    @Test
    void editPage_WithId_ShouldReturnEditForm() throws Exception {
        Book book = new Book(1L, "Book_Title", new Author(1L, "John Doe"), new Genre(1L, "Fiction"));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(new Author()));
        when(genreRepository.findAll()).thenReturn(Collections.singletonList(new Genre()));

        mockMvc.perform(get("/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("allAuthors"))
                .andExpect(model().attributeExists("allGenres"));
    }

    @Test
    void editPage_WithoutId_ShouldReturnEmptyForm() throws Exception {
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(new Author()));
        when(genreRepository.findAll()).thenReturn(Collections.singletonList(new Genre()));

        mockMvc.perform(get("/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_edit"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("allAuthors"))
                .andExpect(model().attributeExists("allGenres"));
    }

    @Test
    void saveBook_ValidData_ShouldRedirect() throws Exception {
        Author author = new Author();
        author.setId(1L);
        Genre genre = new Genre();
        genre.setId(1L);
        Book savedBook = new Book();
        savedBook.setId(1L);

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(genreRepository.findById(1L)).thenReturn(Optional.of(genre));
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/edit")
                        .param("title", "Test Book")
                        .param("author.id", "1")
                        .param("genre.id", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void deleteBook_ExistingId_ShouldRedirect() throws Exception {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(post("/deleteBook")
                        .param("bookId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(bookRepository, times(1)).delete(book);
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

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findAll()).thenReturn(Collections.singletonList(new Author()));
        when(genreRepository.findAll()).thenReturn(Collections.singletonList(new Genre()));
        when(commentRepository.findByBookId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/viewbook").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("view_comments"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("allAuthors"))
                .andExpect(model().attributeExists("allGenres"))
                .andExpect(model().attributeExists("comments"));
    }

    @Test
    void addComment_ShouldSaveComment() throws Exception {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(post("/addComment")
                        .param("bookId", "1")
                        .param("commentText", "Great book!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/viewbook?id=1"));

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void editComment_ShouldUpdateComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        Book book = new Book();
        book.setId(1L);
        comment.setBook(book);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        mockMvc.perform(post("/editComment")
                        .param("commentId", "1")
                        .param("commentText", "Updated comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/viewbook?id=1"));

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void deleteComment_ShouldRemoveComment() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        Book book = new Book();
        book.setId(1L);
        comment.setBook(book);

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        mockMvc.perform(post("/deleteComment")
                        .param("commentId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/viewbook?id=1"));

        verify(commentRepository, times(1)).deleteById(1L);
    }
}
