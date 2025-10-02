package ru.cr.hw.services;

import org.springframework.stereotype.Service;
import ru.cr.hw.dto.GenreDto;
import ru.cr.hw.exceptions.EntityNotFoundException;
import ru.cr.hw.models.Genre;
import ru.cr.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public GenreDto findById(String id) {
        Genre genre = genreRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        return GenreDto.fromDomain(genre);
    }
}
