package ru.cr.hw.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cr.hw.domain.Genre;
import ru.cr.hw.repostory.GenreRepository;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/genre")
    public String listPage(@RequestParam(value = "from", required = false) String from, Model model) {
        List<Genre> genres = genreRepository.findAll();
        model.addAttribute("genres", genres);
        model.addAttribute("fromBooks", "books".equals(from));
        return "list_genres";
    }
}
