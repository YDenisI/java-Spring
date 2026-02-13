package ru.cr.hw.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.cr.hw.page.BookPageController;
import ru.cr.hw.rest.BookRestController;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;
import ru.cr.hw.services.CustomUserDetailsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({BookRestController.class, BookPageController.class,})
@Import(SecurityConfiguration.class)
public class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private CommentService commentService;

    @Test
    public void testAnonymousAccessToRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToBook() throws Exception {
        mockMvc.perform(get("/book/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToBookAdd() throws Exception {
        mockMvc.perform(get("/book/add"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiBooksById() throws Exception {
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiBooksPost() throws Exception {
        mockMvc.perform(post("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiBooksPut() throws Exception {
        mockMvc.perform(put("/api/books/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiBooksDelete() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToRoot() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToBook() throws Exception {
        mockMvc.perform(get("/book/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToBookPost() throws Exception {
        mockMvc.perform(post("/book/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToBookPut() throws Exception {
        mockMvc.perform(put("/book/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToBookDelete() throws Exception {
        mockMvc.perform(delete("/book/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToBookAdd() throws Exception {
        mockMvc.perform(get("/book/add"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiBooks() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiBooksById() throws Exception {
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiBooksPost() throws Exception {
        mockMvc.perform(post("/api/books"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiBooksPut() throws Exception {
        mockMvc.perform(put("/api/books/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiBooksDelete() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAdminAccessToBook() throws Exception {
        mockMvc.perform(get("/book/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAdminAccessToBookAdd() throws Exception {
        mockMvc.perform(get("/book/add"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAdminAccessToApiBooksPut() throws Exception {
        String requestBody = """
        {
            "id": 1,
            "title": "This is a test comment",
            "authorId": 1,
            "genreId": 2
        }
        """;

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAdminAccessToApiBooksDelete() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }
}
