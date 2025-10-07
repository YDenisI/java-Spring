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
import ru.cr.hw.dto.CommentCreateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.page.CommentPageController;
import ru.cr.hw.rest.CommentRestController;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;
import ru.cr.hw.services.CustomUserDetailsService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest({ CommentRestController.class, CommentPageController.class})
@Import(SecurityConfiguration.class)
public class CommentRestControllerTest {

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
    public void testAnonymousAccessToApiComments() throws Exception {
        mockMvc.perform(get("/api/books/1/comments"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiCommentsPost() throws Exception {
        mockMvc.perform(post("/api/books/1/comments"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiCommentsPostById() throws Exception {
        mockMvc.perform(post("/api/comments/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiCommentsPut() throws Exception {
        mockMvc.perform(put("/api/comments/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAnonymousAccessToApiCommentsDelete() throws Exception {
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiComments() throws Exception {
        mockMvc.perform(get("/api/books/1/comments"))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiCommentsPost() throws Exception {
        mockMvc.perform(post("/api/books/1/comments"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiCommentsPostById() throws Exception {
        mockMvc.perform(post("/api/comments/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiCommentsPut() throws Exception {
        mockMvc.perform(put("/api/comments/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    public void testUserAccessToApiCommentsDelete() throws Exception {
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isForbidden());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAdminAccessToApiCommentsPostById() throws Exception {
        CommentDto mockResponse = new CommentDto(1L, "This is a test comment", 1L);
        when(commentService.insert(any(CommentCreateDto.class))).thenReturn(mockResponse);
        String requestBody = """
        {
            "bookId": 1,
            "comment": "This is a test comment"
        }
        """;

        mockMvc.perform(post("/api/books/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAdminAccessToApiCommentsPut() throws Exception {
        String requestBody = """
        {
            "id": 1,
            "comment": "This is a test comment"
        }
        """;
        mockMvc.perform(put("/api/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void testAdminAccessToApiCommentsDelete() throws Exception {
        mockMvc.perform(delete("/api/comments/1"))
                .andExpect(status().isNoContent());
    }
}
