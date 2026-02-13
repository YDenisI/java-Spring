package ru.cr.hw.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.services.GenreService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genre")
    public String listPage(@RequestParam(value = "from", required = false) String from, Model model) {
        List<GenreDto> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        model.addAttribute("fromBooks", "books".equals(from));
        return "list_genres";
    }
}
