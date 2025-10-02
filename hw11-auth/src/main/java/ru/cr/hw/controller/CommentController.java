package ru.cr.hw.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.cr.hw.dto.BookDto;
import ru.cr.hw.dto.CommentCreateDto;
import ru.cr.hw.dto.CommentDto;
import ru.cr.hw.dto.CommentUpdateDto;
import ru.cr.hw.services.BookService;
import ru.cr.hw.services.CommentService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final BookService bookService;

    @PostMapping("/addComment")
    public String addComment(@Valid @ModelAttribute("newComment") CommentCreateDto commentCreateDto,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            BookDto book = bookService.findById(commentCreateDto.getBookId());
            List<CommentDto> comments = commentService.findByBookId(book.getId());
            model.addAttribute("book", book);
            model.addAttribute("comments", comments);
            model.addAttribute("newComment", commentCreateDto);
            return "redirect:/viewbook";
        }

        CommentDto commentDto = commentService.insert(commentCreateDto);
        redirectAttributes.addAttribute("id", commentDto.getBookId());
        return "redirect:/viewbook";
    }

    @PostMapping("/editComment")
    public String editComment(@Valid @ModelAttribute("editingComment") CommentUpdateDto commentUpdateDto,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

      /*  if (bindingResult.hasErrors()) {
            BookDto book = bookService.findById(commentDto.getBookId());
            List<CommentDto> comments = commentService.findByBookId(book.getId());
            model.addAttribute("book", book);
            model.addAttribute("comments", comments);
            model.addAttribute("editingComment", commentDto);
            model.addAttribute("editingCommentId", commentDto.getId());
            return "redirect:/viewbook";
        }*/

        commentService.update(commentUpdateDto);
        CommentDto updated = commentService.findById(commentUpdateDto.getId());
        redirectAttributes.addAttribute("id", updated.getBookId());

        return "redirect:/viewbook";
    }

    @PostMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") Long commentId,
                                RedirectAttributes redirectAttributes) {

        CommentDto comment = commentService.findById(commentId);
        commentService.deleteById(commentId);
        redirectAttributes.addAttribute("id", comment.getBookId());

        return "redirect:/viewbook";
    }
}
