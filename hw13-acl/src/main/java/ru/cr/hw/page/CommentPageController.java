package ru.cr.hw.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.cr.hw.services.BookService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentPageController {

    private final BookService bookService;

    @GetMapping("/book/{bookId}/comments")
    public String commentsPage(@PathVariable Long bookId, Model model) {
        model.addAttribute("bookId", bookId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "view_comments";
    }
}
