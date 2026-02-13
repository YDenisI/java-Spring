package ru.cr.hw.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cr.hw.dto.AuthorDto;
import ru.cr.hw.services.AuthorService;


import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/author")
    public String listPage(@RequestParam(value = "from", required = false) String from, Model model) {
        List<AuthorDto> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        model.addAttribute("fromBooks", "books".equals(from));
        return "list_authors";
    }
}
