package ru.cr.hw.services;

import org.springframework.stereotype.Service;
import ru.cr.hw.models.Genre;
import ru.cr.hw.repositories.GenreRepository;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public Genre insert(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Genre update(String id, String name) {
        Genre genre = new Genre(id, name);
        return genreRepository.save(genre);
    }

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
}
