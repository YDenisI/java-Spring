package ru.cr.hw.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public String commentsPage(@PathVariable String bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "view_comments";
    }
}
