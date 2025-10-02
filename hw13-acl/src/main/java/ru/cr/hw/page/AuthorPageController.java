package ru.cr.hw.page;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthorPageController {

    @GetMapping("/author")
    public String listPage(@RequestParam(value = "from", required = false) String from, Model model) {
        model.addAttribute("fromBooks", "books".equals(from));
        return "list_authors";
    }
}
