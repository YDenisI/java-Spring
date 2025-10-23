package ru.cr.hw.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.cr.hw.dto.*;
import ru.cr.hw.rest.exceptions.NotFoundException;
import ru.cr.hw.services.BookService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(BookRestController.class)
@TestPropertySource(properties = "mongock.enabled=false")
class BookRestControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BookService bookService;
    private final AuthorDto author1 = new AuthorDto("a1", "Author1");
    private final GenreDto genre1 = new GenreDto("g1", "Genre1");
    private final AuthorDto author2 = new AuthorDto("a2", "Author2");
    private final GenreDto genre2 = new GenreDto("g2", "Genre2");
    private final BookDto book1 = new BookDto("1", "Title1", author1, genre1);
    private final BookDto book2 = new BookDto("2", "Title2", author2, genre2);
    private final BookCreateDto createDto = new BookCreateDto("New Title", "New Author", "New Genre", "Comment");
    private final BookUpdateDto updateDto = new BookUpdateDto("1", "Updated Title", "Updated Author", "Update Genre");

    @Test
    void getAllBooks_shouldReturnFluxOfBooks() {

        when(bookService.findAll()).thenReturn(Flux.fromIterable(List.of(book1, book2)));

        webTestClient.get()
                .uri("/api/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(BookDto.class)
                .hasSize(2)
                .contains(book1, book2);
    }

    @Test
    void getBook_shouldReturnBook_whenExists() {

        when(bookService.findById("1")).thenReturn(Mono.just(book1));

        webTestClient.get()
                .uri("/api/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .isEqualTo(book1);
    }

    @Test
    void getBook_shouldReturn400_whenNotFound() {

        when(bookService.findById("999")).thenReturn(Mono.error(new NotFoundException("Book not found")));

        webTestClient.get()
                .uri("/api/books/999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createBook_shouldCreateAndReturnBook() {

        when(bookService.create(any(BookCreateDto.class))).thenReturn(Mono.just(book1));

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .isEqualTo(book1);
    }

    @Test
    void createBook_shouldReturn400_whenValidationFails() {

        BookCreateDto invalidDto = new BookCreateDto("Title", "Author", "Genre", "Comment");

        webTestClient.post()
                .uri("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void updateBook_shouldUpdateAndReturnBook_whenIdsMatch() {

        when(bookService.update(any(BookUpdateDto.class))).thenReturn(Mono.just(book1));

        webTestClient.put()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(BookDto.class)
                .isEqualTo(book1);
    }

    @Test
    void updateBook_shouldReturn302_whenIdsDoNotMatch() {

        BookUpdateDto invalidUpdateDto = new BookUpdateDto("999", "Title", "Author", "Genre");

        webTestClient.put()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidUpdateDto)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateBook_shouldReturn400_whenNotFound() {

        when(bookService.update(any(BookUpdateDto.class))).thenReturn(Mono.error(new NotFoundException("Book not found")));

        webTestClient.put()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateDto)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void updateBook_shouldReturn500_whenValidationFails() {

        BookUpdateDto invalidDto = new BookUpdateDto("1", "Title", "Author", "Genre");

        webTestClient.put()
                .uri("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void deleteBook_shouldReturn204_whenDeleted() {

        when(bookService.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/books/1")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }

    @Test
    void deleteBook_shouldReturn400_whenNotFound() {

        when(bookService.deleteById("999")).thenReturn(Mono.error(new NotFoundException("Book not found")));

        webTestClient.delete()
                .uri("/api/books/999")
                .exchange()
                .expectStatus().isNotFound();
    }
}
