package ru.cr.hw.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BookPageController {

    @GetMapping("/")
    public String listPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);
        return "list_books";
    }

    @GetMapping("/book/{id}")
    public String editPage(@PathVariable(value = "id", required = false) Long id, Model model) {
        model.addAttribute("bookId", id);
        model.addAttribute("isEdit", id != null);
        return "add_edit";
    }

    @GetMapping("/book/add")
    public String addBookPage(Model model) {
        model.addAttribute("isEdit", false);
        return "add_edit";
    }
}
