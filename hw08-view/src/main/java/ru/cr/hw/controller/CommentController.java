package ru.cr.hw.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cr.hw.domain.Book;
import ru.cr.hw.domain.Comment;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final BookService bookService;

    @PostMapping("/addComment")
    public String addComment(@RequestParam("bookId") Long bookId,
                             @RequestParam("commentText") String commentText,
                             RedirectAttributes redirectAttributes) {

        Optional<Book> book = bookService.findById(bookId);
        if (book.isPresent()) {
            commentService.insert(commentText, bookId);
        }

        redirectAttributes.addAttribute("id", bookId);
        return "redirect:/viewbook";
    }

    @PostMapping("/editComment")
    public String editComment(@RequestParam("commentId") Long commentId,
                              @RequestParam("commentText") String commentText,
                              RedirectAttributes redirectAttributes) {

        Optional<Comment> commentOpt = commentService.findById(commentId);
        if (commentOpt.isPresent()) {
            commentService.update(commentId, commentText);
            redirectAttributes.addAttribute("id", commentOpt.get().getBook().getId());
        }

        return "redirect:/viewbook";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                RedirectAttributes redirectAttributes) {
        Optional<Comment> commentOpt = commentService.findById(commentId);
        if (commentOpt.isPresent()) {
            Long bookId = commentOpt.get().getBook().getId();
            commentService.deleteById(commentId);
            redirectAttributes.addAttribute("id", bookId);
        }
        return "redirect:/viewbook";
    }
}
