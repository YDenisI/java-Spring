package ru.cr.hw.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.cr.hw.converters.GenreConverter;
import ru.cr.hw.services.GenreService;

import java.util.stream.Collectors;

@ShellComponent
public class GenreCommands {

    private final GenreService genreService;

    private final GenreConverter genreConverter;

    public GenreCommands(GenreService genreService, GenreConverter genreConverter) {
        this.genreService = genreService;
        this.genreConverter = genreConverter;
    }

    @ShellMethod(value = "Find all genres", key = "ag")
    public String findAllGenres() {
        return genreService.findAll().stream()
                .map(genreConverter::genreToString)
                .collect(Collectors.joining("," + System.lineSeparator()));
    }
}
